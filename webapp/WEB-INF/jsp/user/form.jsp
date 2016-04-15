<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="kr">
<head>
<%@ include file="/include/header.jspf"%>
</head>
<body>
	<%@ include file="/include/navigation.jspf"%>

	<div class="container" id="main">
		<div class="col-md-6 col-md-offset-3">
			<div class="panel panel-default content-main">
				<form:form commandName="user" action="/users" method="POST">
					<div class="form-group">
						<label>사용자 아이디</label>
						<form:input path="userId" cssClass="form-control" placeholder="userId"/>
					</div>
					<div class="form-group">
						<label for="password">비밀번호</label>
						<form:input path="password" cssClass="form-control" placeholder="Password"/>
					</div>
					<div class="form-group">
						<label for="name">이름</label>
						<form:input path="name" cssClass="form-control" placeholder="Name" />
					</div>
					<div class="form-group">
						<label for="email">이메일</label>
						<form:input path="email" cssClass="form-control"
							placeholder="Email" />
					</div>
					<button type="submit" class="btn btn-success clearfix pull-right">회원가입</button>
					<div class="clearfix" />
				</form:form>
			</div>
		</div>
	</div>

	<%@ include file="/include/footer.jspf"%>
</body>
</html>
