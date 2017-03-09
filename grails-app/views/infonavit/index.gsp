<html>
<head>
	<meta charset="UTF-8">
	<title>Infonavit</title>
	<meta name="layout" content="operaciones2"/>
</head>
<body>
	
<content tag="header">
	<h2>Registro de prestamos del INFONAVIT</h2>
</content>

<content tag="reportes">
	<li>
		<g:jasperReport jasper="InfonavitGeneralVigentes"
					format="PDF" name="Vigentes">
		</g:jasperReport>
	</li>
</content>

<content tag="operaciones">
	<li>
		<g:link action="calcularBimestre" 
			onclick="return confirm('Calcular cuota para  bimestre actual?')">
			<span class="glyphicon glyphicon-cog"></span> Re calcular
		</g:link>
	</li>
</content>

<content tag="gridPanel">
	<g:render template="gridPanel"/>
</content>

	
	
	
</body>
</html>