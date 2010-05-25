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
package gwt.g2d.client.graphics;

import gwt.g2d.client.graphics.canvas.CanvasElement;
import gwt.g2d.client.graphics.canvas.CanvasInitializer;
import gwt.g2d.client.graphics.canvas.CanvasPattern;
import gwt.g2d.client.graphics.canvas.Context;
import gwt.g2d.client.graphics.canvas.ImageData;
import gwt.g2d.client.graphics.canvas.ImageDataAdapter;
import gwt.g2d.client.graphics.shapes.Shape;
import gwt.g2d.client.math.Matrix;
import gwt.g2d.client.math.Rectangle;
import gwt.g2d.client.math.Vector2;
import gwt.g2d.client.media.VideoElement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * The surface that an application uses to render to the screen.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.g2d-Surface { }</li>
 * </ul>
 * 
 * @see <a href="http://dev.w3.org/html5/spec/Overview.html#the-canvas-element">
 * http://dev.w3.org/html5/spec/Overview.html#the-canvas-element</a>
 * 
 * @author hao1300@gmail.com
 */
public class Surface extends FocusWidget {	
	private static final CanvasInitializer canvasInitializer = 
			GWT.create(CanvasInitializer.class);
	private final CanvasElement canvas;
	private final Context context;
	
	/**
	 * Initialize a surface with a default size of 100 by 100.
	 */
	public Surface() {
		this(100, 100);
	}
	
	/**
	 * Initialize a surface of the given size.
	 * 
	 * @param width width of the surface.
	 * @param height height of the surface.
	 */
	public Surface(int width, int height) {
		canvas = Document.get().createElement("canvas").cast();
		setElement(Document.get().createDivElement());
		getElement().appendChild(canvas);
		canvasInitializer.init(canvas, width, height);
		setStylePrimaryName("g2d-Surface");
		context = canvas.getContext2D();
	}
	
	/**
	 * Initialize a surface of the given size.
	 * 
	 * @param size the size of the surface to initialize.
	 */
	public Surface(Vector2 size) {
		this(size.getIntX(), size.getIntY());
	}
	
	/**
	 * Gets the size of the surface.
	 */
	public Vector2 getSize() {
		return new Vector2(getWidth(), getHeight());
	}

	/**
	 * Gets the width of the surface.
	 */
	public int getWidth() {
		return getOffsetWidth();
	}
	
	/**
	 * Gets the height of the surface.
	 */
	public int getHeight() {
		return getOffsetHeight();
	}
	
	/**
	 * Resizes the surface.
	 * 
	 * @param width the new width of the surface.
	 * @param height the new height of the surface.
	 */
	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	
	/**
	 * Sets the width of the surface.
	 */
	public void setWidth(int width) {
		canvasInitializer.setWidth(canvas, width);
	}
	
	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		canvasInitializer.setWidth(canvas, this.getWidth());
	}
	
	/**
	 * Sets the height of the surface.
	 */
	public void setHeight(int height) {
		canvasInitializer.setHeight(canvas, height);
	}
	
	@Override
	public void setHeight(String height) {
		super.setHeight(height);
		canvasInitializer.setHeight(canvas, this.getHeight());
	}
	
	/**
	 * Gets the rectangle that encloses this surface.
	 */
	public Rectangle getViewRectangle() {
		return new Rectangle(0, 0, getWidth(), getHeight());
	}

	/**
	 * Gets the canvas element.
	 * 
	 * @return the underlying canvas element.
	 */
	public CanvasElement getCanvas() {
		return canvas;
	}
	
	/**
	 * Gets the context 2D.
	 * 
	 * @return the underlying context implementation for drawing onto the canvas.
	 */
	public Context getContext() {
		return context;
	}
	
	/**
	 * Pushes the current state onto the stack.
	 * 
	 * Drawing states consist of:
	 * <ul>
	 * <li>The current transformation matrix.</li>
	 * <li>The current clipping region.</li>
	 * <li>The current values of the following attributes: strokeStyle, fillStyle, 
	 * globalAlpha, lineWidth, lineCap, lineJoin, miterLimit, shadowOffsetX, 
	 * shadowOffsetY, shadowBlur, shadowColor, globalCompositeOperation, font, 
	 * textAlign, textBaseline.</li>
	 * </ul>
	 * 
	 * Note: The current path and the current bitmap are not part of the drawing 
	 * state.
	 * 
	 * @return self to support chaining.
	 */
	public Surface save() {
		context.save();
		return this;
	}
	
	/**
	 * Pops the top state on the stack, restoring the context to that state.
	 * Sees {@link #save()} method on what can being saved and restored.
	 * 
	 * @return self to support chaining.
	 */
	public Surface restore() {
		context.restore();
		return this;
	}
	
	/**
	 * Scales by (x, y) units.
	 * 
	 * @param x
	 * @param y
	 * @return self to support chaining.
	 */
	public Surface scale(double x, double y) {
		context.scale(x, y);
		return this;
	}
	
	/**
	 * Scales by (scales.x, scales.y) units.
	 * 
	 * @param scales
	 * @return self to support chaining.
	 */
	public Surface scale(Vector2 scales) {
		return scale(scales.getX(), scales.getY());
	}
	
	/**
	 * Scales uniformly by scale units.
	 * 
	 * @param scale
	 * @return self to support chaining.
	 */
	public Surface scale(double scale) {
		return scale(scale, scale);
	}
	
	/**
	 * Rotates clockwise by the given angle about the origin.
	 * 
	 * @param angle
	 * @return self to support chaining.
	 */
	public Surface rotate(double angle) {
		context.rotate(angle);
		return this;
	}
	
	/**
	 * Rotates anti-clockwise by the given angle about the origin.
	 * Use {@link #rotateCcw(double)} as this method name is too long and
	 * ccw is more widely used than anticlockwise.
	 * 
	 * @param angle
	 * @return self to support chaining.
	 */
	@Deprecated
	public Surface rotateAnticlockwise(double angle) {
		return rotate(-angle);
	}
	
	/**
	 * Rotates counter-clockwise by the given angle about the origin.
	 * 
	 * @param angle
	 * @return self to support chaining.
	 */
	public Surface rotateCcw(double angle) {
		return rotate(-angle);
	}
	
	/**
	 * Translates the origin of the surface by (x, y) units.
	 * 
	 * @param x
	 * @param y
	 * @return self to support chaining.
	 */
	public Surface translate(double x, double y) {
		context.translate(x, y);
		return this;
	}
	
	/**
	 * Translates the origin of the surface by (x, y) units.
	 * 
	 * @param translation
	 * @return self to support chaining.
	 */
	public Surface translate(Vector2 translation) {
		return translate(translation.getX(), translation.getY());
	}
	
	/**
	 * Multiply the current transformation by the given transformation matrix.
	 * 
	 * The matrix has the following structure:
	 * <pre> 
   * m11 m21 dx
   * m12 m22 dy
   *  0   0   1 
   * </pre>
   * 
	 * @param m11 
	 * @param m12
	 * @param m21
	 * @param m22
	 * @param dx
	 * @param dy
	 * @return self to support chaining.
	 */
	public Surface transform(double m11, double m12, double m21, double m22,
      double dx, double dy) {
		context.transform(m11, m12, m21, m22, dx, dy);
		return this;
	}
	
	/**
	 * Multiply the current transformation by the given transformation matrix.
   * 
	 * @param matrix
	 * @return self to support chaining.
	 */
	public Surface transform(Matrix matrix) {
		return transform(matrix.getM11(), matrix.getM12(), matrix.getM21(), 
				matrix.getM22(), matrix.getDx(), matrix.getDy());
	}
	
	/**
	 * Sets the current transformation to be the given transformation matrix.
	 * 
	 * The matrix has the following structure:
	 * <pre> 
   * m11 m21 dx
   * m12 m22 dy
   *  0   0   1 
   * </pre>
   * 
	 * @param m11 
	 * @param m12
	 * @param m21
	 * @param m22
	 * @param dx
	 * @param dy
	 * @return self to support chaining.
	 */
	public Surface setTransform(double m11, double m12, double m21, double m22,
      double dx, double dy) {
		context.setTransform(m11, m12, m21, m22, dx, dy);
		return this;
	}
	
	/**
	 * Sets the current transformation to be the given transformation matrix.
   * 
	 * @param matrix
	 * @return self to support chaining.
	 */
	public Surface setTransform(Matrix matrix) {
		return setTransform(matrix.getM11(), matrix.getM12(), matrix.getM21(), 
				matrix.getM22(), matrix.getDx(), matrix.getDy());
	}
	
	/**
	 * Sets the current alpha value applied to rendering operations.
	 * Default: 1.0.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setGlobalAlpha(double alpha) {
		context.setGlobalAlpha(alpha);
		return this;
	}
	
	/**
	 * Gets the current alpha value applied to rendering operations.
	 */
	public double getGlobalAlpha() {
		return context.getGlobalAlpha();
	}
	
	/**
	 * Sets the current composition operation.
	 * Default: {@link Composition#SOURCE_OVER}.
	 * 
	 * @param compositeOperation
	 * @return self to support chaining.
	 */
	public Surface setGlobalCompositeOperation(Composition compositeOperation) {
		context.setGlobalCompositeOperation(compositeOperation.toString());
		return this;
	}
	
	/**
	 * Gets the current composition operation.
	 */
	public Composition getGlobalCompositeOperation() {
		return Composition.parseComposition(context.getGlobalCompositeOperation());
	}
	
	/**
	 * Sets the fill style.
	 * 
	 * @param color the color for the fill style.
	 * @return self to support chaining.
	 */
	public Surface setFillStyle(Color color) {
		context.setFillStyle(color.getColorCode());
		return this;
	}
	
	/**
	 * Sets the fill style.
	 * 
	 * @param gradient the gradient for the fill style.
	 * @return self to support chaining.
	 */
	public Surface setFillStyle(Gradient gradient) {
		context.setFillStyle(gradient.getGradientAdapter(context));
		return this;
	}
	
	/**
	 * Sets the fill style.
	 * 
	 * @param pattern the pattern for the fill style.
	 * @return self to support chaining.
	 */
	public Surface setFillStyle(CanvasPattern pattern) {
		context.setFillStyle(pattern);
		return this;
	}
	
	/**
	 * Sets the stroke style.
	 */
	public Surface setStrokeStyle(Color color) {
		context.setStrokeStyle(color.getColorCode());
		return this;
	}
	
	/**
	 * Sets the stroke style.
	 */
	public Surface setStrokeStyle(Gradient gradient) {
		context.setStrokeStyle(gradient.getGradientAdapter(context));
		return this;
	}
	
	/**
	 * Sets the stroke style.
	 */
	public Surface setStrokeStyle(CanvasPattern pattern) {
		context.setStrokeStyle(pattern);
		return this;
	}
	
	/**
	 * Sets the width of lines, in coordinate space units. On setting, zero, 
	 * negative, infinite, and NaN values must be ignored, leaving the value 
	 * unchanged.
	 * 
	 * Default: 1.0.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setLineWidth(double lineWidth) {
		context.setLineWidth(lineWidth);		
		return this;
	}
	
	/**
	 * Gets the width of lines, in coordinate space units.
	 */
	public double getLineWidth() {
		return context.getLineWidth();
	}
	
	/**
	 * Sets the type of endings that UAs will place on the end of lines.
	 * 
	 * Default: {@link LineCap#BUTT}.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setLineCap(LineCap lineCap) {
		context.setLineCap(lineCap.toString());
		return this;
	}
	
	/**
	 * Gets the type of endings that UAs will place on the end of lines.
	 */
	public LineCap getLineCap() {
		return LineCap.parseLineCap(context.getLineCap());
	}
	
	/**
	 * Sets the type of corners that UAs will place where two lines meet.
	 * 
	 * Default: {@link LineJoin#MITER}.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setLineJoin(LineJoin lineJoin) {
		context.setLineJoin(lineJoin.toString());
		return this;
	}
	
	/**
	 * Gets the type of corners that UAs will place where two lines meet.
	 */
	public LineJoin getLineJoin() {
		return LineJoin.parseLineJoin(context.getLineJoin());
	}
	
	/**
	 * Sets the current miter limit ratio.
	 * 
	 * The miter length is the distance from the point where the join occurs to 
	 * the intersection of the line edges on the outside of the join. The miter 
	 * limit ratio is the maximum allowed ratio of the miter length to half the 
	 * line width. If the miter length would cause the miter limit ratio to be 
	 * exceeded, this second triangle must not be rendered.
	 * 
	 * The miter limit ratio can be explicitly set using the miterLimit attribute. 
	 * On setting, zero, negative, infinite, and NaN values must be ignored, 
	 * leaving the value unchanged.
	 * 
	 * Default: 10.0.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setMiterLimit(double miterLimit) {
		context.setMiterLimit(miterLimit);
		return this;
	}
	
	/**
	 * Gets the current miter limit ratio.
	 */
	public double getMiterLimit() {
		return context.getMiterLimit();
	}
	
	/**
	 * Clears all pixels on the surface in the given rectangle to transparent 
	 * black.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface clearRectangle(double x, double y, double width, double height) {
		context.clearRect(x, y, width, height);
		return this;
	}
	
	/**
	 * Clears all pixels on the surface in the given rectangle to transparent 
	 * black.
	 * 
	 * @param rectangle
	 * @return self to support chaining.
	 */
	public Surface clearRectangle(Rectangle rectangle) {
		return clearRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	/**
	 * Paints the specified rectangular area using the fillStyle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface fillRectangle(double x, double y, double width, double height) {
		context.fillRect(x, y, width, height);
		return this;
	}
	
	/**
	 * Paints the specified rectangular area using the fillStyle.
	 * 
	 * @param rectangle
	 * @return self to support chaining.
	 */
	public Surface fillRectangle(Rectangle rectangle) {
		return fillRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	/**
	 * Strokes the specified rectangle's path using the strokeStyle, lineWidth, 
	 * lineJoin, and (if appropriate) miterLimit attributes
	 */
	public Surface strokeRectangle(double x, double y, double width, double height) {
		context.strokeRect(x, y, width, height);
		return this;
	}
	
	/**
	 * Strokes the specified rectangle's path using the strokeStyle, lineWidth, 
	 * lineJoin, and (if appropriate) miterLimit attributes
	 */
	public Surface strokeRectangle(Rectangle rectangle) {
		return strokeRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	/**
	 * Create a new clipping region by calculating the intersection of the 
	 * current clipping region and the area described by the rectangle, using 
	 * the non-zero winding number rule.
	 */
	public Surface clipRectangle(double x, double y, double width, double height) {
		context.rect(x, y, width, height);
		context.clip();
		return this;
	}
	
	/**
	 * Create a new clipping region by calculating the intersection of the 
	 * current clipping region and the area described by the rectangle, using 
	 * the non-zero winding number rule.
	 */
	public Surface clipRectangle(Rectangle rectangle) {
		return clipRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	/**
	 * Fills the specified shape using the fillStyle.
	 */
	public Surface fillShape(Shape shape) {
		shape.draw(this);
		context.fill();
		return this;
	}
	
	/**
	 * Strokes the specified shape path using the strokeStyle, lineWidth, 
	 * lineJoin, and (if appropriate) miterLimit attributes
	 */
	public Surface strokeShape(Shape shape) {
		shape.draw(this);
		context.stroke();
		return this;
	}
	
	/**
	 * Create a new clipping region by calculating the intersection of the 
	 * current clipping region and the area described by the given shape, using 
	 * the non-zero winding number rule.
	 */
	public Surface clipShape(Shape shape) {
		shape.draw(this);
		context.clip();
		return this;
	}
	
	/**
	 * Fills the background with the given color.
	 */
	public Surface fillBackground(Color color) {
		return setFillStyle(color).fillRectangle(getViewRectangle());
	}
	
	/**
	 * Fills the background with the given gradient.
	 */
	public Surface fillBackground(Gradient gradient) {
		return setFillStyle(gradient).fillRectangle(getViewRectangle());
	}
	
	/**
	 * Clears all pixels to the surface to transparent black.
	 * 
	 * @return self to support chaining.
	 */
	public Surface clear() {
		context.clear();
		return this;
	}
	
	/**
	 * This is equivalent to calling drawFocusRing(widget, x, y, false).
	 * @see #drawFocusRing(Widget, double, double)
	 */
	public boolean drawFocusRing(Widget widget, double x, double y) {
		return context.drawFocusRing(widget.getElement(), x, y);
	}
	
	/**
	 * Use this method to indicate which focusable part of the canvas is 
	 * currently focused, passing it the widget for which a ring is being drawn. 
	 * This method only draws the focus ring if the widget is focused, so that 
	 * it can simply be called whenever drawing the widget, without checking 
	 * whether the widget is focused or not first. The position of the center 
	 * of the control, or of the editing caret if the control has one, should be 
	 * given in the x and y arguments.
	 * 
	 * WARNING: This method may not be implemented in most browsers yet!
	 * 
 	 * @param canDrawCustom if true, then the focus ring is only drawn if the 
 	 * 				user has configured his system to draw focus rings in a particular 
	 * 				manner. (For example, high contrast focus rings.)
	 */
	public boolean drawFocusRing(Widget widget, double x, double y, 
			boolean canDrawCustom) {
		return context.drawFocusRing(widget.getElement(), x, y, canDrawCustom);
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, double x, double y) {
		context.drawImage(image, x, y);
		return this;
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param position the position to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, Vector2 position) {
		return drawImage(image, position.getX(), position.getY());
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, double x, double y, double width, 
			double height) {
		context.drawImage(image, x, y, width, height);
		return this;
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param rectangle the rectangle inside which the image is to be drawn.
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, Rectangle rectangle) {
		return drawImage(image, rectangle.getX(), rectangle.getY(), 
				rectangle.getWidth(), rectangle.getHeight());
	}
	
	/**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image image the image to draw.
	 * @param sourceX
	 * @param sourceY
	 * @param sourceWidth
	 * @param sourceHeight
	 * @param destinationX
	 * @param destinationY
	 * @param destinationWidth
	 * @param destinationHeight
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, double sourceX, double sourceY, 
			double sourceWidth, double sourceHeight, double destinationX, 
			double destinationY, double destinationWidth, double destinationHeight) {
		context.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight,
				destinationX, destinationY, destinationWidth, destinationHeight);
		return this;
	}
	
	/**
	 * /**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image
	 * @param sourceRectangle
	 * @param destinationRectangle
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, Rectangle sourceRectangle, 
			Rectangle destinationRectangle) {
		return drawImage(image, sourceRectangle.getX(), sourceRectangle.getY(),
				sourceRectangle.getWidth(), sourceRectangle.getHeight(),
				destinationRectangle.getX(), destinationRectangle.getY(), 
				destinationRectangle.getWidth(), destinationRectangle.getHeight());
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, double x, double y) {
		context.drawImage(image, x, y);
		return this;
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param position the position to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, Vector2 position) {
		return drawImage(image, position.getX(), position.getY());
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, double x, double y, double width, 
			double height) {
		context.drawImage(image, x, y, width, height);
		return this;
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param rectangle the rectangle inside which the image is to be drawn.
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, Rectangle rectangle) {
		return drawImage(image, rectangle.getX(), rectangle.getY(), 
				rectangle.getWidth(), rectangle.getHeight());
	}
	
	/**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image image the image to draw.
	 * @param sourceX
	 * @param sourceY
	 * @param sourceWidth
	 * @param sourceHeight
	 * @param destinationX
	 * @param destinationY
	 * @param destinationWidth
	 * @param destinationHeight
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, double sourceX, double sourceY, 
			double sourceWidth, double sourceHeight, double destinationX, 
			double destinationY, double destinationWidth, double destinationHeight) {
		context.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight,
				destinationX, destinationY, destinationWidth, destinationHeight);
		return this;
	}
	
	/**
	 * /**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image
	 * @param sourceRectangle
	 * @param destinationRectangle
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, Rectangle sourceRectangle, 
			Rectangle destinationRectangle) {
		return drawImage(image, sourceRectangle.getX(), sourceRectangle.getY(),
				sourceRectangle.getWidth(), sourceRectangle.getHeight(),
				destinationRectangle.getX(), destinationRectangle.getY(), 
				destinationRectangle.getWidth(), destinationRectangle.getHeight());
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(VideoElement image, double x, double y) {
		context.drawImage(image, x, y);
		return this;
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param position the position to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(VideoElement image, Vector2 position) {
		return drawImage(image, position.getX(), position.getY());
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface drawImage(VideoElement image, double x, double y, double width, 
			double height) {
		context.drawImage(image, x, y, width, height);
		return this;
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param rectangle the rectangle inside which the image is to be drawn.
	 * @return self to support chaining.
	 */
	public Surface drawImage(VideoElement image, Rectangle rectangle) {
		return drawImage(image, rectangle.getX(), rectangle.getY(), 
				rectangle.getWidth(), rectangle.getHeight());
	}
	
	/**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image image the image to draw.
	 * @param sourceX
	 * @param sourceY
	 * @param sourceWidth
	 * @param sourceHeight
	 * @param destinationX
	 * @param destinationY
	 * @param destinationWidth
	 * @param destinationHeight
	 * @return self to support chaining.
	 */
	public Surface drawImage(VideoElement image, double sourceX, double sourceY, 
			double sourceWidth, double sourceHeight, double destinationX, 
			double destinationY, double destinationWidth, double destinationHeight) {
		context.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight,
				destinationX, destinationY, destinationWidth, destinationHeight);
		return this;
	}
	
	/**
	 * /**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image
	 * @param sourceRectangle
	 * @param destinationRectangle
	 * @return self to support chaining.
	 */
	public Surface drawImage(VideoElement image, Rectangle sourceRectangle, 
			Rectangle destinationRectangle) {
		return drawImage(image, sourceRectangle.getX(), sourceRectangle.getY(),
				sourceRectangle.getWidth(), sourceRectangle.getHeight(),
				destinationRectangle.getX(), destinationRectangle.getY(), 
				destinationRectangle.getWidth(), destinationRectangle.getHeight());
	}
	
	/**
	 * Instantiate new blank ImageData objects whose dimension is equal to
	 * width x height.
	 * 
	 * @param width
	 * @param height
	 * @return a new ImageData object.
	 */
	public ImageDataAdapter createImageData(int width, int height) {
		return new ImageDataAdapter(context.createImageData(width, height)
				.<ImageData>cast());
	}
	
	/**
	 * Creates a CanvasPattern object that uses the given image and repeats in 
	 * the direction(s) given by the repetition argument.
	 * 
	 * @param image
	 * @param repetition
	 * @return a new CanvasPattern object.
	 */
	public CanvasPattern createPattern(CanvasElement image, 
			PatternRepetition repetition) {
		return context.createPattern(image, repetition.toString());
	}
	
	/**
	 * Creates a CanvasPattern object that uses the given image and repeats in 
	 * the direction(s) given by the repetition argument.
	 * 
	 * @param image
	 * @param repetition
	 * @return a new CanvasPattern object.
	 */
	public CanvasPattern createPattern(ImageElement image, 
			PatternRepetition repetition) {
		return context.createPattern(image, repetition.toString());
	}
	
	/**
	 * Creates a CanvasPattern object that uses the given image and repeats in 
	 * the direction(s) given by the repetition argument.
	 * 
	 * @param image
	 * @param repetition
	 * @return a new CanvasPattern object.
	 */
	public CanvasPattern createPattern(VideoElement image, 
			PatternRepetition repetition) {
		return context.createPattern(image, repetition.toString());
	}
	
	/**
	 * Instantiate new blank ImageData objects whose dimension is equal to
	 * the dimension given, where (x, y) represents (width, height).
	 * 
	 * @param dimension
	 * @return a new ImageData object.
	 */
	public ImageDataAdapter createImageData(Vector2 dimension) {
		return createImageData(dimension.getIntX(), dimension.getIntY());
	}
	
	/**
	 * Instantiate new blank ImageData objects whose dimension is equal to
	 * the given imageData.
	 * 
	 * @param imageData
	 * @return a new ImageData object.
	 */
	public ImageDataAdapter createImageData(ImageDataAdapter imageData) {
		return new ImageDataAdapter(context.createImageData(imageData.getImageData()));
	}

	/**
	 * Returns an ImageData object representing the underlying pixel data for the 
	 * area of the context denoted by the rectangle whose corners are the four 
	 * points (x, y), (x + width, y), (x + width, y + height), (x, y + height), 
	 * in context coordinate space units. Pixels outside the context must be 
	 * returned as transparent black. Pixels must be returned as 
	 * non-premultiplied alpha values.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public ImageDataAdapter getImageData(double x, double y, double width, 
			double height) {
		return new ImageDataAdapter(context.getImageData(x, y, width, height));
	}
	
	/**
	 * Returns an ImageData object representing the underlying pixel data for the 
	 * area of the context denoted by the given rectangle, in context coordinate 
	 * space units. Pixels outside the context must be returned as transparent 
	 * black. Pixels must be returned as non-premultiplied alpha values.
	 * 
	 * @param rect
	 */
	public ImageDataAdapter getImageData(Rectangle rect) {
		return getImageData(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	/**
	 * <p>Paints the data from the given ImageData object onto the context. Only 
	 * the pixels from the dirty rectangle are painted.</p>
	 * <p>The globalAlpha and globalCompositeOperation attributes, as well as the 
	 * shadow attributes, are ignored for the purposes of this method call; 
	 * pixels in the context are replaced wholesale, with no composition, alpha 
	 * blending, no shadows, etc.</p>
	 * 
	 * @param imageData
	 * @param x
	 * @param y
	 * @param dirtyX
	 * @param dirtyY
	 * @param dirtyWidth
	 * @param dirtyHeight
	 */
	public void putImageData(ImageDataAdapter imageData, double x, double y, 
			double dirtyX, double dirtyY, double dirtyWidth, double dirtyHeight) {
		context.putImageData(imageData.getImageData(), x, y, dirtyX, dirtyY, 
				dirtyWidth, dirtyHeight);
	}
	
	/**
	 * <p>Paints the data from the given ImageData object onto the context. Only 
	 * the pixels from the dirty rectangle are painted.</p>
	 * <p>The globalAlpha and globalCompositeOperation attributes, as well as the 
	 * shadow attributes, are ignored for the purposes of this method call; 
	 * pixels in the context are replaced wholesale, with no composition, alpha 
	 * blending, no shadows, etc.</p>
	 * 
	 * @param imageData
	 * @param position
	 * @param dirtyRect
	 */
	public void putImageData(ImageDataAdapter imageData, Vector2 position, 
			Rectangle dirtyRect) {
		context.putImageData(imageData.getImageData(), position.getX(), 
				position.getY(), dirtyRect.getX(), dirtyRect.getY(), 
				dirtyRect.getWidth(), dirtyRect.getHeight());
	}
	
	/**
	 * <p>Paints the data from the given ImageData object onto the context.</p>
	 * <p>The globalAlpha and globalCompositeOperation attributes, as well as the 
	 * shadow attributes, are ignored for the purposes of this method call; 
	 * pixels in the context are replaced wholesale, with no composition, alpha 
	 * blending, no shadows, etc.</p>
	 * 
	 * @param imageData
	 * @param x
	 * @param y
	 */
	public void putImageData(ImageDataAdapter imageData, double x, double y) {
		putImageData(imageData, x, y, 0, 0, imageData.getWidth(), imageData.getHeight());
	}
	
	/**
	 * <p>Paints the data from the given ImageData object onto the context.</p>
	 * <p>The globalAlpha and globalCompositeOperation attributes, as well as the 
	 * shadow attributes, are ignored for the purposes of this method call; 
	 * pixels in the context are replaced wholesale, with no composition, alpha 
	 * blending, no shadows, etc.</p>
	 * 
	 * @param imageData
	 * @param position
	 */
	public void putImageData(ImageDataAdapter imageData, Vector2 position) {
		putImageData(imageData, position.getX(), position.getY());
	}
	
	/**
	 * Sets the font settings. The syntax is the same as for the CSS 'font' 
	 * property; values that cannot be parsed as CSS font values are ignored.
	 */
	public Surface setFont(String font) {
		context.setFont(font);
		return this;
	}
	
	/**
	 * Gets the font settings.
	 */
	public String getFont() {
		return context.getFont();
	}
	
	/**
	 * Sets the text alignment settings.
	 */
	public Surface setTextAlign(TextAlign textAlign) {
		context.setTextAlign(textAlign.toString());
		return this;
	}
	
	/**
	 * Gets the text alignment settings.
	 */
	public TextAlign getTextAlign() {
		return TextAlign.parseTextAlign(context.getTextAlign());
	}
	
	/**
	 * Sets the text baseline alignment settings.
	 */
	public Surface setTextBaseline(TextBaseline textBaseline) {
		context.setTextBaseline(textBaseline.toString());
		return this;
	}
	
	/**
	 * Gets the text baseline alignment settings.
	 */
	public TextBaseline getTextBaseline() {
		return TextBaseline.parseTextBaseline(context.getTextBaseline());
	}
	
	/**
	 * Renders the given text at the given (x, y).
	 */
	public Surface fillText(String text, double x, double y) {
		context.fillText(text, x, y);
		return this;
	}
	
	/**
	 * Renders the given text at the given position.
	 */
	public Surface fillText(String text, Vector2 position) {
		return fillText(text, position.getX(), position.getY());
	}
	
	/**
	 * Renders the given text at the given (x, y), ensuring that the text is
	 * not wider than maxWidth.
	 */
	public Surface fillText(String text, double x, double y, double maxWidth) {
		context.fillText(text, x, y, maxWidth);
		return this;
	}
	
	/**
	 * Renders the given text at the given position, ensuring that the text is
	 * not wider than maxWidth.
	 */
	public Surface fillText(String text, Vector2 position, double maxWidth) {
		return fillText(text, position.getX(), position.getY(), maxWidth);
	}
	
	/**
	 * Renders the given text at the given (x, y).
	 */
	public Surface strokeText(String text, double x, double y) {
		context.strokeText(text, x, y);
		return this;
	}
	
	/**
	 * Renders the given text at the given position.
	 */
	public Surface strokeText(String text, Vector2 position) {
		context.strokeText(text, position.getX(), position.getY());
		return this;
	}
	
	/**
	 * Renders the given text at the given (x, y), ensuring that the text is
	 * not wider than maxWidth.
	 */
	public Surface strokeText(String text, double x, double y, double maxWidth) {
		context.strokeText(text, x, y, maxWidth);
		return this;
	}
	
	/**
	 * Renders the given text at the given position, ensuring that the text is
	 * not wider than maxWidth.
	 */
	public Surface strokeText(String text, Vector2 position, double maxWidth) {
		context.strokeText(text, position.getX(), position.getY(), maxWidth);
		return this;
	}
	
	/**
	 * Returns the advance width with the metrics of the given text in the 
	 * current font.
	 */
	public double measureText(String text) {
		return context.measureText(text);
	}
	
	/**
	 * Sets the distance that the shadow will be offset in the positive 
	 * horizontal direction.
	 * 
	 * @param shadowOffsetX
	 */
	public Surface setShadowOffsetX(double shadowOffsetX) {
		context.setShadowOffsetX(shadowOffsetX);
		return this;
	}
	
	/**
	 * Gets the distance that the shadow will be offset in the positive
	 * horizontal direction.
	 */
	public double getShadowOffsetX() {
		return context.getShadowOffsetX();		
	}

	/**
	 * Sets the distance that the shadow will be offset in the positive 
	 * vertical direction.
	 * 
	 * @param shadowOffsetY
	 */
	public Surface setShadowOffsetY(double shadowOffsetY) {
		context.setShadowOffsetY(shadowOffsetY);
		return this;
	}

	/**
	 * Gets the distance that the shadow will be offset in the positive
	 * vertical direction.
	 */
	public double getShadowOffsetY() {
		return context.getShadowOffsetY();
	}
	
	/**
	 * Sets the distance that the shadow will be offset in the positive 
	 * horizontal and vertical direction.
	 * 
	 * @param shadowOffset
	 */
	public Surface setShadowOffset(Vector2 shadowOffset) {
		context.setShadowOffsetX(shadowOffset.getX());
		context.setShadowOffsetY(shadowOffset.getY());
		return this;
	}

	/**
	 * Gets the distance that the shadow will be offset in the positive
	 * horizontal and vertical direction.
	 */
	public Vector2 getShadowOffset() {
		return new Vector2(context.getShadowOffsetX(), context.getShadowOffsetY());
	}
	
	/**
	 * Gets the size of the blurring effect.
	 * 
	 * @param shadowBlur
	 */
	public Surface setShadowBlur(double shadowBlur) {
		context.setShadowBlur(shadowBlur);
		return this;
	}
	
	/**
	 * Gets the size of the blurring effect.
	 */
	public double getShadowBlur() {
		return context.getShadowBlur();
	}

	/**
	 * Sets the color of the shadow.
	 * 
	 * @param shadowColor
	 */
	public Surface setShadowColor(Color shadowColor) {
		context.setShadowColor(shadowColor.getColorCode());
		return this;
	}
}
