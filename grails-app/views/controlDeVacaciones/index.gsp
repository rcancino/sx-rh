<html>
<head>
	<meta charset="UTF-8">
	<title>Vacaciones</title>
	<meta name="layout" content="operaciones2"/>
</head>
<body>

<content tag="header">
	<h3>Control de vacaciones ${session.ejercicio }</h3>
</content>

<content tag="operaciones">

	<li><g:link action="generar" 
				onclick="return confirm('Generar el control de vacaciones ?','${ejercicio}');" 
				id="${ejercicio}">
				<span class="glyphicon glyphicon-cog"></span> Generar
		</g:link></li>
</content>
<content tag="reportes">
	<li>
		<g:jasperReport jasper="ControlDeVacaciones"
				format="PDF" name="General">
			<g:hiddenField name="EJERCICIO" 
					value="${ejercicio}" />
		</g:jasperReport>
		
	</li>
</content>
<content tag="gridPanel">
	<g:render template="vacacionesGridPanel"/>
</content>

	
	
</body>
</html>

