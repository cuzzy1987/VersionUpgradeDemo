package com.me.versionupdatedemo.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cs on 2019/4/2.
 */
public class DateUtils {

	public static int dateAfter(String former,String later){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date dateFormer = dateFormat.parse(former);
			Date dateLater = dateFormat.parse(later);
			if (dateFormer.getTime() < dateLater.getTime()){
				return 1;
			}
			return 0;
		}catch (Exception e){
			System.out.println(e.getMessage()+" former=> "+former+" later=> "+later);
			return -1;
		}
	}

	/* 根据起始日期计算截止日期是符合规则 */

	/**
	 *
	 * @param from 起始日期
	 * @param to 截止日期
	 * @return
	 */
	public static DateCheckBean  getNextMonthDay(String from,String to){
		String fromArr[] = from.split("-");
		int fromYear = Integer.valueOf(fromArr[0]);
		int fromMonth = Integer.valueOf(fromArr[1]);
		int fromDay = Integer.valueOf(fromArr[2]);

		String toArr[] = to.split("-");
		int toYear = Integer.valueOf(toArr[0]);
		int toMonth = Integer.valueOf(toArr[1]);
		int toDay = Integer.valueOf(toArr[2]);
		String msg = "";
		if (fromYear==toYear && fromMonth==toMonth && toDay-fromDay<28){
			 return new DateCheckBean("",false,"最短租期为一个月");
		 }
		/* 	前提条件是日期 from 在日期 to 之前
			返回正确的年月日
		*/
		/*
		* 起始月份是否是二月 ==> 计算是否是当前月份的最后一天
		*
		* */

		/*
		  比如从今天开始 那就是下月今日的前一天
		  1.28 1.29的话就到2.27 2.28 。
		  1.30 1.31这种就到二月最后一天 有29就29 没有就28
		*/
		String realToDay = "";
		if (fromDay==1){
			/* 起始时间是1号 */
			System.out.println("check out => isFirstDay");
			realToDay = monthLastDay(toYear,toMonth) + "";
		}else if (checkIsLastDay(fromYear,fromMonth,fromDay)){
			/* 起始时间是本月最后一天 */
			System.out.println("check out => isLastDay");
			if (toMonth == 2 ) {
				if (fromMonth==2){
					realToDay = (monthLastDay(toYear, toMonth)-1)+"";
				}else realToDay = (fromDay-1)+"";
			}else realToDay = (fromDay-1)+"";
		}else {
			System.out.println("check out => isNormalDay");
			// 如果
			if (toMonth == 2 && fromDay == monthLastDay(fromYear,fromMonth)-1 &&  fromMonth!=2){
				realToDay = "" + monthLastDay(toYear,toMonth);
			} else realToDay = (fromDay -1)+"";

		}
		return new DateCheckBean(realToDay,realToDay.equals(""+toDay),msg);
	}

	public static class DateCheckBean{

		DateCheckBean(java.lang.String shouldDay, boolean isCorrect,String msg) {
			this.shouldDay = shouldDay;
			this.isCorrect = isCorrect;
			this.msg = msg;
		}

		String msg;
		String shouldDay;
		boolean isCorrect;

		@Override
		public String toString() {
			if (!TextUtils.isEmpty(msg))return msg;
			return "日期是否正确 => "+ (isCorrect?"是":"否")+
					"\n正确日期应为 =>" + shouldDay;
		}
	}

	private static boolean checkIsLastDay(int fromYear,int fromMonth,int fromDay) {
		boolean isLastDay = false;

		if (fromMonth==2){
			if (isLeapYear(fromYear)){
				if (fromDay==29) isLastDay = true;
			}else{
				if (fromDay==28) isLastDay = true;
			}
		}else {
			if (is31Day(fromMonth)){
				if (fromDay==31)isLastDay = true;
			}else {
				if (fromDay==30)isLastDay = true;
			}
		}
		return isLastDay;
	}

	/*A：能被4整除，并且不能被100整除。或者
	B：能被400整除。*/
	private static boolean isLeapYear(int year){
		return  (year%400==0) && ((year%100!=0)&&year%4==0);
	}

	/* 1 3 5 7 8 10 12 ||  4 6 9 11*/
	private static boolean is31Day(int month){
		return month!=2&&month!=4&&month!=6&&month!=9&&month!=11;
	}

	/* 返回上个月 月份的最后一天 */
	/* 1 3 5 7 8 10 12 ||  4 6 9 11*/
	private static int monthLastDay(int year,int month){
		System.out.println(String.format("toYear=> %d,toMonth=> %d",year,month));
		switch (month){
			case 0:
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return 31;
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
			case 2:
				return isLeapYear(year)?29:28;
			default:return -1;
		}
	}

}
