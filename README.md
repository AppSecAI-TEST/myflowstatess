# myflowstatess
防支付宝快递状态信息。  各状态文字 数量可自定义修改 

[![](https://jitpack.io/v/ky48302430/myflowstatess.svg)](https://jitpack.io/#ky48302430/myflowstatess)
![Alt text](https://github.com/ky48302430/myflowstatess/blob/master/myflowstates/Screenshots/0811_09_13_01.png)



Step 1.

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


Step 2. Add the dependency

	dependencies {
	        compile 'com.github.ky48302430:myflowstatess:1.0'
	}
	
Xml 布局

     <declare-styleable name="FlowStatesView">
             <attr name="ShowTexts" />
             <attr name="CheckColor" />
             <attr name="TextSize" />
             <attr name="StartInfo" />
             <attr name="EndInfo" />
     </declare-styleable>

     <cyber.myflowstates.FolwStatesView
        android:id="@+id/fsview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        s:ShowTexts ="已发货,运输中,派件中,已签收"
        s:StartInfo = "苏州市"
        s:EndInfo   = "潍坊市"
        />
	
代码

     
        final FolwStatesView  statesView = (FolwStatesView) findViewById(R.id.fsview);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                statesView.setSycnSelected(3);  
            }
        }, 5000);

