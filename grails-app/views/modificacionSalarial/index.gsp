<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Modificaciones salariales</title>
	
</head>
<body>
	<content tag="header">
		<h2>Modificación salarial Bimestre: ${session.bimestre } (${session.ejercicio })</h2>
		
	</content>
	
	
	<content tag="buttonsBar">
		<g:link action="index" class="btn btn-success btn-outline">
				Refrescar
			<span class="glyphicon glyphicon-repeat"></span> 
		</g:link>
		<g:link action="create" class="btn btn-success btn-outline">
			<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
		</g:link>
		
		<a href="#cambioDeBimestreDialog" class="btn btn-success btn-outline" data-toggle="modal">
			<span class="glyphicon glyphicon-calendar"></span> Cambiar bimestre
		</a>
	</content>

	<content tag="operacionesPanel">
		
	</content>

	<content tag="reportes">
		<li>
			<a  data-toggle="modal"	data-target="#sdiModificacioForm"> Modificacion Salarial </a>
		</li>
	</content>


	<content tag="gridPanel">
				<g:render template="grid"/>
				<g:render template="cambioDeBimestreDialog"/>
				<g:render template="sdiModificacion"/>
	</content>
	
</body>
</html>