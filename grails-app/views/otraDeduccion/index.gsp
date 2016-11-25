<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Otras deducciones</title>
</head>
<body>

<content tag="header">
	<h2>Módulo de administración de otras deducciones (${session.ejercicio})</h2>
	<lx:warningLabel/>
</content>

<content tag="reportes">
	<li>
		<g:jasperReport jasper="OtrasDeduccionesGeneral"
					format="PDF" name="Vigentes">
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