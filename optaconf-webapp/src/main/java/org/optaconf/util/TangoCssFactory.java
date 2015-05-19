/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaconf.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TangoCssFactory {

	public static final String CHAMELEON_1 = "chameleon-1";
	public static final String CHAMELEON_2 = "chameleon-2";
	public static final String CHAMELEON_3 = "chameleon-3";
	public static final String BUTTER_1 = "butter-1";
	public static final String BUTTER_2 = "butter-2";
	public static final String BUTTER_3 = "butter-3";
	public static final String SKY_BLUE_1 = "sky-blue-1";
	public static final String SKY_BLUE_2 = "sky-blue-2";
	public static final String SKY_BLUE_3 = "sky-blue-3";
	public static final String CHOCOLATE_1 = "chocolate-1";
	public static final String CHOCOLATE_2 = "chocolate-2";
	public static final String CHOCOLATE_3 = "chocolate-3";
	public static final String PLUM_1 = "plum-1";
	public static final String PLUM_2 = "plum-2";
	public static final String PLUM_3 = "plum-3";

	public static final String SCARLET_1 = "scarlet-1";
	public static final String SCARLET_2 = "scarlet-2";
	public static final String SCARLET_3 = "scarlet-3";
	public static final String ORANGE_1 = "orange-1";
	public static final String ORANGE_2 = "orange-2";
	public static final String ORANGE_3 = "orange-3";

	public static final String ALUMINIUM_1 = "aluminium-1";
	public static final String ALUMINIUM_2 = "aluminium-2";
	public static final String ALUMINIUM_3 = "aluminium-3";
	public static final String ALUMINIUM_4 = "aluminium-4";
	public static final String ALUMINIUM_5 = "aluminium-5";
	public static final String ALUMINIUM_6 = "aluminium-6";

	public static final String[] COLORS = { ALUMINIUM_1, ALUMINIUM_2,
			ALUMINIUM_3, ALUMINIUM_4, ALUMINIUM_5, ALUMINIUM_6, BUTTER_1,
			BUTTER_2, BUTTER_3, CHAMELEON_1, CHAMELEON_2, CHAMELEON_3,
			CHOCOLATE_1, CHOCOLATE_2, CHOCOLATE_3, ORANGE_1, ORANGE_2,
			ORANGE_3, PLUM_1, PLUM_2, PLUM_3, SCARLET_1, SCARLET_2, SCARLET_3,
			SKY_BLUE_1, SKY_BLUE_2, SKY_BLUE_3 };

	private Map<String, String> colorMap;
	private Random r;
	
	public TangoCssFactory() {
		colorMap = new HashMap<String, String>();
		r = new Random(0);
	}

	public String pickCssClass(String id) {
		String cssClass = colorMap.get(id);
		if (cssClass == null) {
			cssClass = nextCssClass();
			while(colorMap.values().contains(cssClass)){
				cssClass = nextCssClass();				
			}
			colorMap.put(id, cssClass);
		}
		return cssClass;
	}

	private String nextCssClass() {
		int Low = 0;
		int High = COLORS.length;
		
		int R = r.nextInt(High-Low) + Low;
		
		String cssClass = COLORS[R]; 
		
		return cssClass;
	}
	
}
