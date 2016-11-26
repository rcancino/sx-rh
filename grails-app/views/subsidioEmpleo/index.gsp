<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="catalogos"/>
	<title>Subsidio al empleo</title>
</head>
<body>
	<content tag="header">
		Tabla de Subsidio al Empleo 
	</content>
	<content tag="grid">
		<table id="grid" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>Ejercicio</th>
					<th>Desde</th>
					<th>Hasta</th>
					<th>Subsidio</th>
					
				</tr>
			</thead>
			<tbody>
				<g:each in="${subsidioEmpleoInstanceList}" var="row">
					<tr>
						<td>${row.ejercicio}</td>
						<td>
							<g:link action="show" id="${row.id}">
								<g:formatNumber number="${row.desde}" format="#,###.##"/>
							</g:link>
						</td>
						<td><g:formatNumber number="${row.hasta}" format="#,###.##"/></td>
						<td><g:formatNumber number="${row.subsidio}" format="#,###.##"/></td>
						
					</tr>
				</g:each>
			</tbody>
		</table>
		
	</content>
</body>
</html>