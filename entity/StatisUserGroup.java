package entity;

import java.io.Serializable;
import java.sql.Date;

public class StatisUserGroup implements Serializable
{

	private static final long serialVersionUID = -2115928126993678012L;

	private long id;
	private long userGroupId;
	private Date date;
	private int hour;// 0-23
	private String pageName;
	private String metricName;
	private String metricValue;
	private Date reportDate;

	public int getHour()
	{
		return hour;
	}

	public void setHour(int hour)
	{
		this.hour = hour;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getUserGroupId()
	{
		return userGroupId;
	}

	public void setUserGroupId(long userGroupId)
	{
		this.userGroupId = userGroupId;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getPageName()
	{
		return pageName;
	}

	public void setPageName(String pageName)
	{
		this.pageName = pageName;
	}

	public String getMetricName()
	{
		return metricName;
	}

	public void setMetricName(String metricName)
	{
		this.metricName = metricName;
	}

	public String getMetricValue()
	{
		return metricValue;
	}

	public void setMetricValue(String metricValue)
	{
		this.metricValue = metricValue;
	}

	public Date getReportDate()
	{
		return reportDate;
	}

	public void setReportDate(Date reportDate)
	{
		this.reportDate = reportDate;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + hour;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((metricName == null) ? 0 : metricName.hashCode());
		result = prime * result + ((metricValue == null) ? 0 : metricValue.hashCode());
		result = prime * result + ((pageName == null) ? 0 : pageName.hashCode());
		result = prime * result + ((reportDate == null) ? 0 : reportDate.hashCode());
		result = prime * result + (int) (userGroupId ^ (userGroupId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatisUserGroup other = (StatisUserGroup) obj;
		if (date == null)
		{
			if (other.date != null)
				return false;
		}
		else if (!date.equals(other.date))
			return false;
		if (hour != other.hour)
			return false;
		if (id != other.id)
			return false;
		if (metricName == null)
		{
			if (other.metricName != null)
				return false;
		}
		else if (!metricName.equals(other.metricName))
			return false;
		if (metricValue == null)
		{
			if (other.metricValue != null)
				return false;
		}
		else if (!metricValue.equals(other.metricValue))
			return false;
		if (pageName == null)
		{
			if (other.pageName != null)
				return false;
		}
		else if (!pageName.equals(other.pageName))
			return false;
		if (reportDate == null)
		{
			if (other.reportDate != null)
				return false;
		}
		else if (!reportDate.equals(other.reportDate))
			return false;
		if (userGroupId != other.userGroupId)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "StatisUserGroupData [id=" + id + ", userGroupId=" + userGroupId + ", date=" + date + ", hour=" + hour
				+ ", pageName=" + pageName + ", metricName=" + metricName + ", metricValue=" + metricValue
				+ ", reportDate=" + reportDate + "]";
	}

}
