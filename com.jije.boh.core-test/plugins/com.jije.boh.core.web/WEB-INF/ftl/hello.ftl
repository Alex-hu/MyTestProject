<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript"
	src="/resources/scripts/test.js"></script>
<script type="text/javascript" src="/resources/scripts/jquery.min.js"></script>
</head>
<body>
I'm ftl page. ${date}
<div id="moduletest" />
<script type="text/javascript" >
function test() {
$.ajax(
	{
		type: "POST",
		url: "/app/service",
		data: "module=module.alextest&controller=cash&function=test",
		success: function(json)
		{
			$("#moduletest").html(json);
		},
		error: function(date)
		{
			alert("Err!");
		}
	});
}
test();
</script>
</body>
</html>