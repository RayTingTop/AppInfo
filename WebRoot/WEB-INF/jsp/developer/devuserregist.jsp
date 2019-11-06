<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="common/header.jsp"%>
<div class="clearfix"></div>
<div class="row">
  <div class="col-md-12 col-sm-12 col-xs-12">
    <div class="x_panel">
      <div class="x_title">
        <h2>申请新开发人员账号 <i class="fa fa-user"></i><small>${devUserSession.devName}</small></h2>
             <div class="clearfix"></div>
      </div>
      <div class="x_content">
         <div class="clearfix"></div>
        <form id="devuserform" class="form-horizontal form-label-left" action="devuserregistsave" method="post">
          <div class="item form-group">
            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">登录名 <span class="required">*</span>
            </label>
            <div class="col-md-6 col-sm-6 col-xs-12">
              <input id="devCode" class="form-control col-md-7 col-xs-12" 
               data-validate-length-range="20" data-validate-words="1" name="devCode"  required="required"
               placeholder="请输入用户名,用于登录" type="text">
            </div>
          </div>
          <div class="item form-group">
            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">开发名称 <span class="required">*</span>
            </label>
            <div class="col-md-6 col-sm-6 col-xs-12">
              <input id="devName" class="form-control col-md-7 col-xs-12" 
               data-validate-length-range="20" data-validate-words="1" name="devName"  required="required"
               placeholder="请输入开发者名称" type="text">
            </div>
          </div>
          <div class="item form-group">
            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">密码 <span class="required">*</span>
            </label>
            <div class="col-md-6 col-sm-6 col-xs-12">
              <input id="devPassword" class="form-control col-md-7 col-xs-12" 
              	data-validate-length-range="20" data-validate-words="1" name="devPassword"   required="required"
              	placeholder="请输入登录密码" type="text">
            </div>
          </div>
          <div class="item form-group">
            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">确认密码 <span class="required">*</span>
            </label>
            <div class="col-md-6 col-sm-6 col-xs-12">
              <input id="reDevPassword" class="form-control col-md-7 col-xs-12" 
              	data-validate-length-range="20" data-validate-words="1" name="reDevPassword"   required="required"
              	placeholder="请再次输入密码,确认密码无误" type="text">
            </div>
          </div>
          
          <div class="item form-group">
            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">E-mail<span class="required">*</span>
            </label>
            <div class="col-md-6 col-sm-6 col-xs-12">
              <input id="devEmail" class="form-control col-md-7 col-xs-12" name="devEmail" 
              	data-validate-length-range="20" data-validate-words="1"  
              	placeholder="请输入Email(非必填)" type="text">
            </div>
          </div>
          
	      <div class="item form-group">
	          <label class="control-label col-md-3 col-sm-3 col-xs-12" for="textarea">用户简介 <span class="required">*</span>
	           </label>
	           <div class="col-md-6 col-sm-6 col-xs-12">
	             <textarea id="devInfo" name="devInfo" 
	             placeholder="请输入开发人员的相关信息(非必填)" class="form-control col-md-7 col-xs-12"></textarea>
	          </div>
	      </div>
          </div>
         
           <div class="item form-group">
            ${fileUploadError }
            </div>
          </div>
          <div class="ln_solid"></div>
          <div class="form-group">
            <div class="col-md-6 col-md-offset-3">
              <button id="send" type="submit" class="btn btn-success">保存</button>
              <button type="button" class="btn btn-primary" id="back">返回</button>
              <br/><br/>
            </div>
          </div>
        </form>
      </div>
    </div>
<%@include file="common/footer.jsp"%>
<script src="${pageContext.request.contextPath }/statics/localjs/devuserregist.js"></script>