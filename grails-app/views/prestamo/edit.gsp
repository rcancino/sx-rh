<html>
<head>
	<meta charset="UTF-8">
	<title>Prestamo ${prestamoInstance.id}</title>
</head>
<body>

	<lx:header>
		<h2 class="text-left">Prestamo: ${prestamoInstance.id}  ${prestamoInstance.empleado} </h2>
		<h3>Saldo: <g:formatNumber number="${prestamoInstance.saldo}"   type="currency"/></h3>
		<lx:errorsHeader bean="${prestamoInstance}"/>
		<lx:warningLabel/>
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