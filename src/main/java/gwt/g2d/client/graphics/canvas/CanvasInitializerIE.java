/*
 * Copyright 2009 Hao Nguyen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.g2d.client.graphics.canvas;

import com.google.gwt.dom.client.Style.Unit;

/**
 * Helper class for initializing a canvas element for IE.
 * 
 * @author hao1300@gmail.com
 */
public class CanvasInitializerIE extends CanvasInitializer {
	
	@Override
	public void init(CanvasElement element, int width, int height) {
		initExcanvas(element);
		setWidth(element, width);
		setHeight(element, height);
	}
	
	@Override
	public void setWidth(CanvasElement element, int width) {
		element.getStyle().setWidth(width, Unit.PX);
	}
	
	@Override
	public void setHeight(CanvasElement element, int height) {
		element.getStyle().setHeight(height, Unit.PX);
	}
	
	/**
	 * Initializing excanvas support for the canvas element.
	 * 
	 * @param element
	 */
	private native void initExcanvas(CanvasElement element) /*-{
		$wnd.G_vmlCanvasManager.initElement(element);
	}-*/;
}
