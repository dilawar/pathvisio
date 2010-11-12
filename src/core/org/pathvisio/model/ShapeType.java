// PathVisio,
// a tool for data visualization and analysis using Biological Pathways
// Copyright 2006-2009 BiGCaT Bioinformatics
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.pathvisio.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
   Extensible enum
 */
public class ShapeType
{
	private static Map<String, ShapeType> mappMappings = new HashMap<String, ShapeType>();
	private static Map<String, ShapeType> nameMappings = new HashMap<String, ShapeType>();
	private static List<ShapeType> values = new ArrayList<ShapeType>();

	public static final ShapeType NONE = new ShapeType ("None", "None");
	public static final ShapeType RECTANGLE = new ShapeType ("Rectangle", "Rectangle");
	public static final ShapeType ROUNDED_RECTANGLE = new ShapeType ("RoundedRectangle", "RoundedRectangle");
	public static final ShapeType OVAL = new ShapeType ("Oval", "Oval");
	public static final ShapeType ARC = new ShapeType ("Arc", "Arc");
	public static final ShapeType TRIANGLE = new ShapeType ("Triangle", "Poly"); // poly ;in MAPP
	public static final ShapeType PENTAGON = new ShapeType ("Pentagon", "Poly"); // poly in MAPP
	public static final ShapeType HEXAGON = new ShapeType ("Hexagon", "Poly"); // poly in MAPP
	public static final ShapeType BRACE = new ShapeType ("Brace", "Brace");	
	public static final ShapeType MITOCHONDRIA = new ShapeType ("Mitochondria", null);
	public static final ShapeType SARCOPLASMICRETICULUM = new ShapeType ("Sarcoplasmic Reticulum", null);
	public static final ShapeType ENDOPLASMICRETICULUM = new ShapeType ("Endoplasmic Reticulum", null);
	public static final ShapeType GOLGIAPPARATUS = new ShapeType ("Golgi Apparatus", null);
	
	// BEGIN DEPRECATION MANAGEMENT CODE
	@Deprecated
	public static final ShapeType CELL = new ShapeType ("Cell", null);
	@Deprecated
	public static final ShapeType NUCLEUS = new ShapeType ("Nucleus", null);
	@Deprecated
	public static final ShapeType ORGANELLE = new ShapeType ("Organelle", null);
	@Deprecated
	public static final ShapeType VESICLE = new ShapeType ("Vesicle", "Vesicle");
	@Deprecated
	public static final ShapeType MEMBRANE = new ShapeType ("Membrane", "Membrane");
	@Deprecated
	public static final ShapeType CELLA = new ShapeType ("CellA", "CellA");
	@Deprecated
	public static final ShapeType RIBOSOME = new ShapeType ("Ribosome", "Ribosome");
	@Deprecated
	public static final ShapeType ORGANA = new ShapeType ("OrganA", "OrganA");
	@Deprecated
	public static final ShapeType ORGANB = new ShapeType ("OrganB", "OrganB");
	@Deprecated
	public static final ShapeType ORGANC = new ShapeType ("OrganC", "OrganC");
	@Deprecated
	public static final ShapeType PROTEINB = new ShapeType ("ProteinComplex", "ProteinB");

	
	//This map is used to track deprecated shapetypes for conversion and exclusion from gui
	public static final Map<ShapeType, ShapeType> deprecatedMap = new HashMap<ShapeType, ShapeType>();
	static { 
		deprecatedMap.put(CELL, ROUNDED_RECTANGLE);
		deprecatedMap.put(ORGANELLE, ROUNDED_RECTANGLE);
		deprecatedMap.put(MEMBRANE, ROUNDED_RECTANGLE);
		deprecatedMap.put(CELLA, OVAL);
		deprecatedMap.put(NUCLEUS, OVAL);
		deprecatedMap.put(ORGANA, OVAL);
		deprecatedMap.put(ORGANB, OVAL);
		deprecatedMap.put(ORGANC, OVAL);
		deprecatedMap.put(VESICLE, OVAL);   	
		deprecatedMap.put(PROTEINB, HEXAGON);
		deprecatedMap.put(RIBOSOME, HEXAGON);
		// exclude from list for gui
		pruneValues();
	}
	
	/**
	 * Prunes values list for deprecated shapes
	 */
	private static void pruneValues() {
		List<ShapeType> list = new ArrayList<ShapeType>();
		for (int i = 0; i < values.size(); ++i)
		{
			ShapeType s = values.get(i);
			if (!deprecatedMap.containsKey(s)){
				list.add(s);
			}
		}
		values = list;
	}
	// END DEPRECATION MANAGEMENT CODE
   
	private String name;
	private String mappName;
	private boolean isResizeable;
	private boolean isRotatable;

	/**
	   The constructor is private so we have to use the "create"
	   method to add new ShapeTypes. In the create method we make sure
	   that the same object can't get added twice.

	   Note that mappName may be null for Shapes that are not supported by GenMAPP.
	 */
	private ShapeType(String name, String mappName)
	{
		this(name, mappName, true, true);
	}

	private ShapeType (String name, String mappName, boolean isResizeable, boolean isRotatable)
	{
		if (name == null) { throw new NullPointerException(); }
		this.isResizeable = isResizeable;
		this.isRotatable = isRotatable;
		this.mappName = mappName;
		this.name  = name;

		if (mappName != null)
		{
			mappMappings.put (mappName, this);
		}
		nameMappings.put (name, this);

		// add it to the array list.
		values.add (this);
	}

	/**
	   Create an object and add it to the list.

	   For extending the enum.
	 */
	public static ShapeType create (String name, String mappName)
	{
		if (nameMappings.containsKey (name))
		{
			return nameMappings.get (name);
		}
		else
		{
			return new ShapeType (name, mappName, true, true);
		}
	}

   /**
	   Create an object and add it to the list.

	   For extending the enum.
	 */
	public static ShapeType create (String name, String mappName, boolean isResizeable, boolean isRotatable)
	{
		if (nameMappings.containsKey (name))
		{
			return nameMappings.get (name);
		}
		else
		{
			return new ShapeType (name, mappName, isResizeable, isRotatable);
		}
	}

    /*
	 * Warning when using fromMappName: in case value == Poly, 
	 * this will return Triangle. The caller needs to check for
	 * this special
	 * case.
	 */
	public static ShapeType fromMappName (String value)
	{
		return mappMappings.get(value);
	}

	public String getMappName()
	{
		return mappName;
	}

	/**
	   @deprecated: use ShapeType.fromName() instead, it does the same thing.
	 */
	public static ShapeType fromGpmlName (String value)
	{
		return fromName (value);
	}

	/**
	   looks up the ShapeType corresponding to that name.
	 */
	public static ShapeType fromName (String value)
	{
		return nameMappings.get(value);
	}

	/**
	   Stable identifier for this ShapeType.
	 */
	public String getName ()
	{
		return name;
	}

	/**
	   Returns the names of all registered Shape types, in such a way that the index
	   is equal to it's ordinal value.

	   i.e. ShapeType.fromName(ShapeType.getNames[n]).getOrdinal() == n
	 */
	static public String[] getNames()
	{
		String[] result = new String[values.size()];

		for (int i = 0; i < values.size(); ++i)
		{
			result[i] = values.get(i).getName();
		}
		return result;
	}

	static public ShapeType[] getValues()
	{
		return values.toArray(new ShapeType[0]);
	}

	public String toString()
	{
		return name;
	}

	public boolean isResizeable()
	{
		return isResizeable;
	}

	public boolean isRotatable()
	{
		return isRotatable;
	}

}