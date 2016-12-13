<html>
<head>
	<meta charset="UTF-8">
	<title>Turno ${turnoInstance.id}</title>
</head>
<body>

	<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
	

		<div class="row">
			<lx:errorsHeader bean="${turnoInstance}"/>
			<div class="col-md-9">
				<g:render template="editForm"/>
			</div>
		</div>
	</div>
	
</body>
</html>