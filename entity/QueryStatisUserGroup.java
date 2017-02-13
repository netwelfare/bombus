package entity;


import java.util.Date;
import java.util.List;

import cn.bombus.core.annotation.Alias;
@Alias("wxf")
public class QueryStatisUserGroup
{
	private Long userGroupId;
	private String indicatorName;
	private List<String> pageNames;
	private String statFrequency;// HOUR, DAY
	private Integer statDays;
	private Date startDate;
	private Date endDate;

	public Long getUserGroupId()
	{
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId)
	{
		this.userGroupId = userGroupId;
	}

	public String getIndicatorName()
	{
		return indicatorName;
	}

	public List<String> getPageNames()
	{
		return pageNames;
	}

	public void setPageNames(List<String> pageNames)
	{
		this.pageNames = pageNames;
	}

	public void setIndicatorName(String indicatorName)
	{
		this.indicatorName = indicatorName;
	}

	public String getStatFrequency()
	{
		return statFrequency;
	}

	public void setStatFrequency(String statFrequency)
	{
		this.statFrequency = statFrequency;
	}

	public Integer getStatDays()
	{
		return statDays;
	}

	public void setStatDays(Integer statDays)
	{
		this.statDays = statDays;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

}
