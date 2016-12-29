<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="catalogos"/>
	<title>Calendarios</title>
</head>
<body>


	<content tag="header">
			Dias festivos
	</content>
	<content tag="reportes">
		<li>
			<g:link action="reporte" params="[ejercicio:'${session.ejercicio}']"> Calendario Dia F</g:link>
			
		</li>
	</content>
	
	<content tag="grid">
		<g:render template="grid"/>
	</content>
</body>
</html>