<html>
<head>
	<meta charset="UTF-8">
	<title>FONACOT ${fonacotInstance.id}</title>
</head>
<body>

<lx:header>
	<h2>Prestamo FONACOT: ${fonacotInstance.id} (${fonacotInstance.empleado})</h2>
</lx:header>

<div class=" row wrapper wrapper-content   animated fadeInRight">
	<div class="col-md-6">
		<g:render template="editPanel"/>
	</div>

	<div class="col-md-6">
		<g:render template="abonosPanel"/>
	</div>
</div>
	
	
	
</body>
</html>