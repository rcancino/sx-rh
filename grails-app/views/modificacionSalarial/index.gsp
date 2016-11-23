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
	<content tag="grid">
		<g:render template="grid"/>
	</content>
	
	<content tag="buttonsBar">
	<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-repeat"></span> Todos
						</g:link>
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
						</g:link>
						
						<button class="btn btn-default" data-toggle="modal" data-target="#filterDialog">
							<span class="glyphicon glyphicon-search"></span> Buscar
						</button>
						<a href="#cambioDeBimestreDialog" class="btn btn-default" data-toggle="modal">
							<span class="glyphicon glyphicon-calendar"></span> Cambiar bimestre
						</a>
	</content>


	<content tag="gridPanel">
		
			%{-- <div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
					      <a  data-toggle="modal"	data-target="#sdiModificacioForm"> Modificacion Salarial	</a>
					      
					</li>
					
				</ul>
				</div> --}%
		</div>

						
						<g:render template="filter"/>
						<g:render template="cambioDeBimestreDialog"/>
						<g:render template="sdiModificacion"/>
					</div>
	</content>
	
</body>
</html>