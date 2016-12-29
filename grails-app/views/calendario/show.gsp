<html>
<head>
	<meta charset="UTF-8">
	<title>Calendario ${calendarioInstance.id}</title>
</head>
<body>

	<div class="wrapper wrapper-content   animated fadeInRight">
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<lx:ibox>
					<lx:iboxTitle title="Calendario ${calendarioInstance.ejercicio} (${calendarioInstance.tipo})"/>

	              	<lx:iboxContent>
	              		<lx:warningLabel />
						<form  class="form-horizontal" >
			              	<f:with bean="calendarioInstance">
			              		<f:display property="ejercicio" />
			              		<f:display property="tipo" />
			              		<f:field property="comentario" widget-class="form-control" />
			              	</f:with>
			            </form>
	              	</lx:iboxContent>

	              	<lx:iboxFooter>
	                	<lx:backButton class="btn btn-default btn-outline btn-sm" label="Calendarios"/>
	                	<lx:editButton class="btn btn-default btn-outline btn-sm" id="${calendarioInstance.id}"/>
	                	<lx:deleteButton bean="${calendarioInstance}"/>
						<g:link class='btn btn-outline btn-default'type="button" action="generarPeriodosCalendario" id="${calendarioInstance.id}" onclick="return confirm('Generar Periodos para calendario');">Crear Periodos
							<span class="glyphicon glyphicon-calendar"></span>
						</g:link>
					<div class="btn-group">
						<button type="button" name="reporte"
	                     class="btn btn-primary btn-outline dropdown-toggle" data-toggle="dropdown"  id="${calendarioInstance.id}"
	                     role="menu">
	                     Reportes <span class="caret"></span>
	             		</button>
	           			<ul class="dropdown-menu">
			                <li>
								<g:link action="reporte" id="${calendarioInstance.id}"> Calendario Anual</g:link>
								
							</li>
			             </ul>
					</div>

	              	</lx:iboxFooter>
					
				</lx:ibox>

			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<lx:ibox>
					<lx:iboxTitle title="Periodos"/>
					<lx:iboxContent>
						<g:render template="periodosGrid"/>
					</lx:iboxContent>
				</lx:ibox>
			</div>
		</div>
	<div>
	
</body>
</html>