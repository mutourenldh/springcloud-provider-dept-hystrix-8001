package com.haoge.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.haoge.cloud.entities.Dept;
import com.haoge.cloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/** 
* @author 李东浩
* @Date：2018年4月29日下午1:23:23
*
*/
@RestController
public class DeptController {
	@Autowired
	private DeptService service;
	
	/**
	 * 根据Id查询部门
	 * @param id
	 * @return
	 */
	//一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法
	@HystrixCommand(fallbackMethod="processHystrix_Get")
	@RequestMapping(value="/dept/get/{id}",method=RequestMethod.GET)
	public Dept get(@PathVariable Long id) {
		Dept dept= service.get(id);
		if (dept==null) {
			throw new RuntimeException("该ID：" + id + "没有没有对应的信息");
		}
		return dept;
	}
	public Dept processHystrix_Get(@PathVariable("id") Long id) {
		Dept dept = new Dept();
		dept.setDname("该ID：" + id + "没有没有对应的信息,null--@HystrixCommand");
		dept.setDeptno(id);
		dept.setDb_source("no this database in MySQL");
		return dept;
	}

}
