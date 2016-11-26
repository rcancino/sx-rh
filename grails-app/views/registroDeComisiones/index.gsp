<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Comisiones</title>
	
</head>
<body>
<content tag="header">
	<h2>Registro de comisiones</h2>
	<lx:warningLabel/>
</content>

<content tag="reportes">
	<li>
		<g:link action="comisionesPorCobrador" class="btn btn-group">
			<span class="glyphicon glyphicon-print"></span> Por cobrador
		</g:link>
	</li>
</content>

<content tag="operaciones">
	
</content>

<content tag="gridPanel">
	<g:render template="gridPanel"/>
</content>

	
</body>
</html>