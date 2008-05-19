#!/usr/bin/perl

=over
Filterpatch is used to clean up patches that were generated by
comparing a pathway before and after adding "sticky edges"

After adding "sticky edges" to a pathway, coordinates my change as
a side effect. Another complicating factor is that if the work was
done on an older pathway set without graphId's, new graphId's are
generated that may be different. 

To solve both problems, filterpatch filters out irrelevant
changes from a patch. Only changes to "startGraphRef" and "endGraphRef"
are allowed to remain. Changes to graphId's are recorded and mapped back
so you don't see them anymore.

=cut

use XML::Twig;
use strict;
use warnings;

my %idMapping;
my $fnPatch = shift @ARGV;

# build map of changing id's

my $twig1 = XML::Twig->new(
	twig_handlers => {
		Modify => \&findIdMapping,
	},
	pretty_print => 'indented',);

$twig1->parsefile ($fnPatch);
# no need to flush...

# filter out deletions / insertions
# filter out all changes, only leave changes on attr = graphId, attr=startGraphRef, attr=endGraphRef

my $twig2 = XML::Twig->new(
	twig_handlers => {
		Modify => \&modify,
		Delete => \&skip,
		Insert => \&skip,
	},
	pretty_print => 'indented',);
$twig2->parsefile ($fnPatch);

my $output;
open $output, "> $fnPatch" or die $!;
$twig2->flush ($output);
close $output;

sub modify
{
	my $t = shift;
	my $elt = shift;
	
	for my $child ($elt->children ("Change"))
	{
		my $attr = $child->att("attr");
		if ($attr eq "StartGraphRef" ||
			$attr eq "EndGraphRef")
		{
			# replace new id's with old id's
			my $new = $child->att("new");
			$child->set_att("new", $idMapping{$new});
		}
		else
		{
			#~ print "Deleting child with attr $attr\n";
			$child->delete();
		}
	}
	
	if ($elt->children_count ("Change") == 0)
	{
		#~ print "Deleting modify with no remaining changes\n";
		$elt->delete(); # delete if no changes at all
	}
}

sub skip
{
	my $t = shift;
	my $elt = shift;
	#~ print "Deleting element " . $elt->tag() . " on " . $elt->first_child()->tag() . "\n";
	$elt->delete();
}


sub findIdMapping
{
	my $t = shift;
	my $elt = shift;
	
	for my $child ($elt->children ("Change"))
	{
		my $attr = $child->att("attr");
		if ($attr eq "GraphId")
		{
			my $old = $child->att("old");
			my $new = $child->att("new");
			print "GraphId $old chaning to $new\n";
			$idMapping{$new} = $old;
		}
	}
}