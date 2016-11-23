<html>
<head>
	<meta charset="UTF-8">
	<title>Otra Deduccion ${otraDeduccionInstance.id}</title>
</head>
<body>

<lx:header>
	<h3 class="text-left">Deducción: ${otraDeduccionInstance.id}  ${otraDeduccionInstance.empleado} 
		Saldo:   <strong><g:formatNumber number="${otraDeduccionInstance.saldo}"   type="number"/></strong>
	</h3>
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