/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package org.xclcharts.renderer;


import java.util.ArrayList;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.axis.AxisTick;
import org.xclcharts.renderer.axis.CategoryAxis;
import org.xclcharts.renderer.axis.CategoryAxisRender;
import org.xclcharts.renderer.axis.DataAxis;
import org.xclcharts.renderer.axis.DataAxisRender;
import org.xclcharts.renderer.plot.AxisTitle;
import org.xclcharts.renderer.plot.AxisTitleRender;

import android.graphics.Canvas;
import android.graphics.Paint.Align;

/**
 * @ClassName AxisChart
 * @Description 所有用到坐标类的图表的基类,主要用于定义和绘制坐标轴
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class AxisChart extends EventChart {
		
	//数据轴
	protected DataAxisRender dataAxis  = null;
	//标签轴
	protected CategoryAxisRender categoryAxis  = null;	
	//轴标题类
	private AxisTitleRender axisTitle = null;
	
	//格式化柱形顶上或线交叉点的标签
	private IFormatterDoubleCallBack mItemLabelFormatter;
	
	//平移模式下的可移动方向
	private XEnum.PanMode mPlotPanMode = XEnum.PanMode.FREE;
	private boolean mEnablePanMode = true;
	
	
	public AxisChart() {
		// TODO Auto-generated constructor stub			
		initChart();		
	}
	
	
	/**
	 * 初始化设置
	 */
	private void initChart()
	{				
		
		//数据轴
		if(null == dataAxis)initDataAxis(); 
		
		//标签轴
		if(null == categoryAxis)initCategoryAxis();
						
		//初始化图例
		if(null != plotLegend)
		{
			plotLegend.show();
			plotLegend.setType(XEnum.LegendType.ROW);
			plotLegend.setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
			plotLegend.setVerticalAlign(XEnum.VerticalAlign.TOP);
			plotLegend.hideBox();
		}		
	}
		

	 /**
	  * 开放数据轴绘制类
	  * @return 数据轴绘制类
	  */
	public DataAxis getDataAxis() 
	{
		//数据轴
		initDataAxis(); 
		return dataAxis;
	}

	/**
	 * 开放标签轴绘制类
	 * @return 标签轴绘制类
	 */
	public CategoryAxis getCategoryAxis() 
	{
		//标签轴
		initCategoryAxis();
		return categoryAxis;
	}
			
	private void initCategoryAxis()
	{
		if(null == categoryAxis)categoryAxis  = new CategoryAxisRender();
	}
	
	public void initDataAxis() 
	{
		if(null == dataAxis)dataAxis  = new DataAxisRender();	
	}
	
	protected void drawCategoryAxisLabels(Canvas canvas,
										  XEnum.Direction direction,
										  ArrayList<AxisTick> lstLabels)
	{
		if(null == lstLabels) return ;
		for(AxisTick t : lstLabels)
		{
			if(XEnum.Direction.HORIZONTAL == direction)
		    {
				categoryAxis.renderAxisHorizontalTick(this,canvas,t.X,t.Y, t.Label);
		    }else{		    	
		    	categoryAxis.renderAxisVerticalTick(canvas,t.X,t.Y, t.Label);		    	
		    }
		}
	}
	
	protected void drawDataAxisLabels(Canvas canvas,
									 XEnum.Direction direction,
									 ArrayList<AxisTick> lstLabels)
	{
		if(null == lstLabels) return ;
		for(AxisTick t : lstLabels)
		{
			dataAxis.setAxisTickCurrentID(t.ID);
			
			if(XEnum.Direction.HORIZONTAL == direction)
		    {					
				dataAxis.renderAxisVerticalTick(canvas,t.X,t.Y, t.Label);
		    }else{
		    	dataAxis.renderAxisHorizontalTick(this,canvas,t.X,t.Y, t.Label);	
		    }
		}
	}

	/**
	 * 开放轴标题绘制类
	 * @return 图例绘制类
	 */
	public AxisTitle getAxisTitle() 
	{		
		//轴标题
		if(null == axisTitle)axisTitle = new AxisTitleRender();					
		return axisTitle;
	}

	/**
	 * 轴所占的屏幕宽度
	 * @return  屏幕宽度
	 */
	protected float getAxisScreenWidth()
	{
		if(null == plotArea)return 0.0f;
		return(Math.abs(plotArea.getRight() - plotArea.getLeft()));
	}
	
	protected float getPlotScreenWidth()
	{
		if(null == plotArea)return 0.0f;
		return(Math.abs(plotArea.getPlotRight() - plotArea.getPlotLeft()));
	}
	
	protected float getPlotScreenHeight()
	{
		if(null == plotArea)return 0.0f;
		return( Math.abs(plotArea.getPlotBottom() - plotArea.getPlotTop()));
	}
	
	
	/**
	 * 轴所占的屏幕高度
	 * @return 屏幕高度
	 */
	protected float getAxisScreenHeight()
	{
		if(null == plotArea)return 0.0f;
		return( Math.abs(plotArea.getBottom() - plotArea.getTop()));
	}
	
	/**
	 * 设置标签显示格式
	 * 
	 * @param callBack
	 *            回调函数
	 */
	public void setItemLabelFormatter(IFormatterDoubleCallBack callBack) {
		this.mItemLabelFormatter = callBack;
	}

	/**
	 * 返回标签显示格式
	 * 
	 * @param value 传入当前值
	 * @return 显示格式
	 */
	protected String getFormatterItemLabel(double value) {
		String itemLabel = "";
		try {
			itemLabel = mItemLabelFormatter.doubleFormatter(value);
		} catch (Exception ex) {
			itemLabel = Double.toString(value);
			// DecimalFormat df=new DecimalFormat("#0");
			// itemLabel = df.format(value).toString();
		}
		return itemLabel;
	}
	
	
	/**
	 * 竖向柱形图 Y轴的屏幕高度/数据轴的刻度标记总数 = 步长
	 * 
	 * @return Y轴步长
	 */
	//private float getVerticalYSteps(int tickCount) {
	//	return MathHelper.getInstance().div(getAxisScreenHeight() , tickCount);
	//}

	/**
	 * 竖向图 得到X轴的步长 X轴的屏幕宽度 / 刻度标记总数 = 步长
	 * 
	 * @param num
	 *            刻度标记总数
	 * @return X轴步长
	 */
	protected float getVerticalXSteps(int num) {
		//float XSteps = (float) Math.ceil(getAxisScreenWidth() / num);		
		return MathHelper.getInstance().div(getAxisScreenWidth(),num);		
	}	
	
	
	/**
	 * 检查竖图中数据轴的tick是否显示 
	 * @param currentY	y坐标
	 * @param moveY	y坐标平移值
	 * @return	是否绘制
	 */
	protected boolean isRenderVerticalBarDataAxisTick(float currentY,float moveY)
	{
		if(Float.compare(currentY , plotArea.getTop() - moveY) == -1 || 
				Float.compare(currentY, plotArea.getBottom()  - moveY) == 1 )
		{
			return true;
		}
		return false;
	}
			
	/**
	 *  检查竖图中分类轴的tick是否显示 
	 * @param currentX	x坐标
	 * @param moveX	x坐标平移值
	 * @return 是否绘制
	 */
	protected boolean isRenderVerticalCategoryAxisTick(float currentX,float moveX)
	{
		if(Float.compare(currentX , plotArea.getLeft() - moveX ) == -1 || 
				Float.compare(currentX , plotArea.getRight() - moveX) == 1 )				
		{
			return true;
		}
		return false;
	}
	
	protected boolean isRenderHorizontalDataAxisTick(float currentX,float moveX)
	{
		if(Float.compare(currentX , plotArea.getLeft() - moveX ) == -1 || 
				Float.compare(currentX , plotArea.getRight() - moveX) == 1 )				
		{
			return true;
		}
		return false;
	}
	
	protected boolean isRenderHorizontalCategoryAxisTick(float currentY,float moveY)
	{
		if(categoryAxis.isShowAxisLabels() && 
				(Float.compare(currentY , plotArea.getTop() - moveY) == -1 || 
				 Float.compare(currentY , plotArea.getBottom() - moveY) == 1 ))
		{
			return true;
		}
		return false;
	}
	
	protected float getDrawClipVerticalYMargin()
	{
		float yMargin = 0.0f;
		if(dataAxis.isShowAxisLabels())
		{
			yMargin = DrawHelper.getInstance().getPaintFontHeight(
									dataAxis.getTickLabelPaint() ) / 2;				
		}
		return yMargin;
	}
	
	protected float getDrawClipVerticalXMargin()
	{
		float xMargin = 0.0f;
		if(categoryAxis.isShowAxisLabels())
		{												
			if(categoryAxis.getHorizontalTickAlign() != Align.LEFT)
			{
				String str = categoryAxis.getDataSet().get(0);					
				xMargin = DrawHelper.getInstance().getTextWidth(
										categoryAxis.getTickLabelPaint(), str); 
				
				if(categoryAxis.getHorizontalTickAlign() == Align.CENTER)
				{
					xMargin = div(xMargin,2);
				}													
			}
		}
		return xMargin;
	}
	
		
	/////////////////////////
	
	protected float getDrawClipYMargin()
	{		
		return getDrawClipVerticalYMargin();
	}
	
	protected float getDrawClipXMargin()
	{
		return getDrawClipVerticalXMargin();
	}
	
	//Pan模式下移动距离
	protected float mMoveX = 0.0f;
	protected float mMoveY = 0.0f;
	
	
	protected void initMoveXY()
	{
		mMoveX = mMoveY = 0.0f;  	
		switch(this.getPlotPanMode())
		{
		case HORIZONTAL:
			mMoveX = mTranslateXY[0]; 			
			break;
		case VERTICAL:
			mMoveY = mTranslateXY[1]; 					
			break;
		default:
			mMoveX = mTranslateXY[0]; 
			mMoveY = mTranslateXY[1]; 
			break;
		}
	}
	
	
	public boolean isShowRightAxis()
	{		
		return false;
	}
	
	protected void drawClipCategoryAxis(Canvas canvas)
	{
		
	}
	
	protected void drawClipDataAxis(Canvas canvas)
	{
		
	}
	
	protected void drawClipPlot(Canvas canvas)
	{
		
	}
	
	protected void drawClipAxisLine(Canvas canvas)
	{
		
	}
		
	protected void drawClipDataAxisTick(Canvas canvas)
	{
		
	}
	
	protected void drawClipCategoryAxisTick(Canvas canvas)
	{
		
	}
	
	protected void drawClipLegend(Canvas canvas)
	{
		
	}
	
	protected boolean drawFixedPlot(Canvas canvas)
	{	
		this.mMoveX = this.mMoveY = 0.0f;
		
		//绘制Y轴tick和marks	
		drawClipDataAxis(canvas);	
				
		//绘制X轴tick和marks	
		drawClipCategoryAxis(canvas);
		
		//绘图
		drawClipPlot(canvas);
		
		//轴 线
		drawClipAxisLine(canvas);				
			
		//轴刻度
		drawClipDataAxisTick(canvas);	
		drawClipCategoryAxisTick(canvas);
				
		//图例
		drawClipLegend(canvas);		
		return true;
	 }
		
	protected float getClipYMargin()
	{
		return getDrawClipVerticalYMargin();
	}
	
	protected float getClipXMargin()
	{
		return getDrawClipVerticalXMargin();
	}
	
	protected boolean drawClipVerticalPlot(Canvas canvas)
	{				
		//显示绘图区rect
		float offsetX = mTranslateXY[0]; 
		float offsetY = mTranslateXY[1];  
		initMoveXY();
								
		float yMargin = getClipYMargin();
		float xMargin = getClipXMargin();
		
		//设置图显示范围
		canvas.save();
		canvas.clipRect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
				
		if( XEnum.PanMode.VERTICAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{
			
			//绘制Y轴tick和marks			
			canvas.save();		
					canvas.clipRect(plotArea.getLeft() , plotArea.getTop() - yMargin, 
							plotArea.getRight(), plotArea.getBottom() + yMargin);
					canvas.translate(0 , offsetY );					
					
					drawClipDataAxis(canvas);					
			canvas.restore();	
		}else{
			drawClipDataAxis(canvas);	
		}
			
		if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{	
			
			//绘制X轴tick和marks			
			canvas.save();		
					canvas.clipRect(plotArea.getLeft() - xMargin, plotArea.getTop(), 
									plotArea.getRight()+ xMargin, this.getBottom());
					canvas.translate(offsetX,0);
					
					drawClipCategoryAxis(canvas);
			canvas.restore();
		}else{
			drawClipCategoryAxis(canvas);
		}
						
			//设置绘图区显示范围
			canvas.save();
			if (isShowRightAxis())
			{
				canvas.clipRect(plotArea.getLeft() , plotArea.getTop(), 
								plotArea.getRight(), plotArea.getBottom() + 0.5f );
			}else{
				canvas.clipRect(plotArea.getLeft() , plotArea.getTop(), 
								this.getRight(), plotArea.getBottom() + 0.5f);
			}
					canvas.save();					
					canvas.translate(mMoveX, mMoveY);
					//绘图
					drawClipPlot(canvas);
					
					canvas.restore();
			canvas.restore();			
			
		//还原绘图区绘制
		canvas.restore(); //clip	
		
		//轴 线
		drawClipAxisLine(canvas);		
		
		/////////////////////////////////////////
		//轴刻度
		if( XEnum.PanMode.VERTICAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{			
			//绘制Y轴tick和marks			
			canvas.save();		
					canvas.clipRect(this.getLeft() , plotArea.getTop() - yMargin, 
									this.getRight(), plotArea.getBottom() + yMargin);
					canvas.translate(0 , offsetY );					
					
					drawClipDataAxisTick(canvas);		
			canvas.restore();	
		}else{
			drawClipDataAxisTick(canvas);	
		}
			
		if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{				
			//绘制X轴tick和marks			
			canvas.save();		
					canvas.clipRect(plotArea.getLeft() - xMargin, plotArea.getTop(), 
									plotArea.getRight()+ xMargin, this.getBottom());
					canvas.translate(offsetX,0);
					drawClipCategoryAxisTick(canvas);
			canvas.restore();
		}else{
			drawClipCategoryAxisTick(canvas);
		}
		/////////////////////////////////////////
			
		//图例
		drawClipLegend(canvas);
		
		execGC();
		return true;
	 }
	
	/////////////////////////  drawClipHorizontalPlot
	protected boolean drawClipHorizontalPlot(Canvas canvas)
	 {		
		//显示绘图区rect
		float offsetX = mTranslateXY[0]; 
		float offsetY = mTranslateXY[1]; 					
		initMoveXY();		
				
		float yMargin =  getClipYMargin();
		float xMargin = getClipXMargin();
	
		//设置图显示范围
		canvas.save();				
		canvas.clipRect(this.getLeft() , this.getTop() , this.getRight(), this.getBottom());		
				
		if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{																	
			//绘制X轴tick和marks			
			canvas.save();		
					canvas.clipRect(plotArea.getLeft() - xMargin, plotArea.getTop(), 
									plotArea.getRight()+ xMargin, this.getBottom());
					canvas.translate(offsetX,0);
					drawClipDataAxis(canvas);						
			canvas.restore();	
		}else{
			drawClipDataAxis(canvas);			
		}
		
		if( XEnum.PanMode.VERTICAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{													
			//绘制Y轴tick和marks			
			canvas.save();		
					canvas.clipRect(plotArea.getLeft(), plotArea.getTop() - yMargin,  //this.getLeft()
									plotArea.getRight(), plotArea.getBottom() + yMargin); //this.getRight()
					canvas.translate(0 , offsetY );					
					drawClipCategoryAxis(canvas);		
			canvas.restore();	
		}else{
			drawClipCategoryAxis(canvas);
		}
		//////////////////////////////////////////////////
		
			//////////////////////////////////////////////////								
			//设置绘图区显示范围
			canvas.save();
			canvas.clipRect(plotArea.getLeft() , plotArea.getTop() ,
							this.getRight(), plotArea.getBottom());			
					canvas.save();
					canvas.translate(mMoveX, mMoveY);	
					//绘图
					drawClipPlot(canvas);
					
					canvas.restore();
			canvas.restore();
		
			
		//还原绘图区绘制
		canvas.restore(); //clip							

		//////////////////////////////////////////////////			
		//轴线
		drawClipAxisLine(canvas);
				
		if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{																	
			//绘制X轴tick和marks			
			canvas.save();		
					canvas.clipRect(plotArea.getLeft() - xMargin, plotArea.getTop(), 
									plotArea.getRight()+ xMargin, this.getBottom());
					canvas.translate(offsetX,0);
											
					drawClipDataAxisTick(canvas);
			canvas.restore();	
		}else{
			drawClipDataAxisTick(canvas);
		}
						
		
		if( XEnum.PanMode.VERTICAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{													
			//绘制Y轴tick和marks			
			canvas.save();		
					canvas.clipRect(this.getLeft(), plotArea.getTop() - yMargin, 
									this.getRight(), plotArea.getBottom() + yMargin);
					canvas.translate(0 , offsetY );					
					
					drawClipCategoryAxisTick(canvas);
			canvas.restore();	
		}else{	
			drawClipCategoryAxisTick(canvas);
		}
		
		//////////////////////////////////////////////////
		
		//图例
		drawClipLegend(canvas);	
		
		execGC();
		return true;
	 }
	/////////////////////////

	////////////////////////
			
	@Override
	protected boolean postRender(Canvas canvas) throws Exception
	{
		try {			
			super.postRender(canvas);			
			
			//计算主图表区范围
			 calcPlotRange();
			//画Plot Area背景			
			 plotArea.render(canvas);	
						 
			//绘制标题
			 renderTitle(canvas);
			//绘制轴标题
			if(null != axisTitle)
			{
				axisTitle.setRange(this);
				axisTitle.render(canvas);
			}
			
		}catch( Exception e){
			 throw e;
		}
		return true;
	}

	
	/**
	 * 设置手势平移模式
	 * @param mode	平移模式
	 */
	public void setPlotPanMode(XEnum.PanMode mode)
	{
		 mPlotPanMode = mode;
	}
	
	/**
	 * 返回当前图表平移模式
	 * @return 平移模式
	 */
	public XEnum.PanMode getPlotPanMode()
	{
		return mPlotPanMode;
	}
	
	/**
	 * 激活平移模式
	 */
	public void enablePanMode()
	{
		mEnablePanMode = true;		
	}
	
	/**
	 * 禁用平移模式
	 */
	public void disablePanMode()
	{
		mEnablePanMode = false;		
	}
	
	/**
	 * 返回当前图表的平移状态
	 * @return
	 */
	public boolean getPanModeStatus()
	{
		return mEnablePanMode;
	}
	
}
