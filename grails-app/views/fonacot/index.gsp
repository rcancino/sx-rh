<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Prestamos</title>
</head>
<body>

<content tag="header">
	<h2>Prestamos FONACTO</h2>
</content>

<content tag="reportes">
	<li>
		<g:jasperReport jasper="FonactotGeneral"
					format="PDF" name="Vigentes">
		</g:jasperReport>
	</li>
</content>

<content tag="operaciones">
	
</content>

<content tag="gridPanel">
	<g:render template="gridPanel"/>
</content>
	
	
	
</body>
</html>