<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Tiempo extra</title>
</head>
<body>

<content tag="header">
	<h2>Módulo de administración de tiempo extra (${session.ejercicio})</h2>
</content>

<content tag="reportes">
	<li>
		<g:jasperReport jasper="TiempoExtraGeneral"
					format="PDF" name="TiempoExtra">
		</g:jasperReport>
	</li>
</content>

<content tag="operaciones">
	<li>
		<a href="#actualizarDialog" data-toggle="modal">
			<span class="glyphicon glyphicon-cog"></span> Actualizar
		</a>
	</li>
</content>

<content tag="gridPanel">
	<g:render template="gridPanel"/>
</content>


</body>
</html>