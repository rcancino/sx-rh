<%@page expressionCodec="none" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Alta de turno</h3>
	</div>
	<div class="panel-body">
	
		<g:form action="save" role="form" class="form-horizontal" >
			<div class="well">
				<f:with bean="turnoInstance">
					<f:field property="descripcion" widget-class="form-control"/>
					<f:field property="inicioDeDia">
						<input class=" form-control time " 
						type="text" 
						name="inicioDeDia" 
						value="${g.formatDate(date:value,format:'HH:mm') }" >
					</f:field>

					<f:field property="horaLimiteDeTrabajo" label="Hora máxima de trabajo">
						<input class=" form-control time " 
						type="text" 
						name="horaLimiteDeTrabajo" 
						value="${g.formatDate(date:value,format:'HH:mm') }" >
					</f:field>

					<f:field property="horaLimiteSiguienteDia" widget-class="form-control" label="Siguiente día"
						value="${turnoInstance.horaLimiteSiguienteDia}"/>
					
					<f:field property="inicioDeTiempoExtra" label="Hora máxima de trabajo">
						<input class=" form-control time " 
						type="text" 
						name="inicioDeTiempoExtra" 
						value="${g.formatDate(date:value,format:'HH:mm') }" >
					</f:field>

					
				</f:with>
			</div>
			<div class="panel-header">
				<h3>Días</h3>
				<table class="table table-striped table-bordered table-condensed">
					<thead>
						<tr >
							<th class="text-center">Día</th>
							<th class="text-center" colspan="2">Grupo 1</th>
							<th class="text-center" colspan="2">Grupo 2</th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${(0..<turnoInstance.dias.size())}" var="row">
						<tr>
							<td>${turnoInstance.dias[row].dia}
								<g:hiddenField name="dias[${row}].dia" 
									value="${turnoInstance.dias[row].dia}"/>
							</td>
							<td>
								<input class="time " 
									type="text" 
									name="dias[${row}].entrada1" 
									value="${turnoInstance.dias[row].entrada1}" >
							</td>
							<td>
								<input class="time " 
									type="text" 
									name="dias[${row}].salida1" 
									value="${turnoInstance.dias[row].salida1}" >
							<td>
								<input class="time " 
									type="text" 
									name="dias[${row}].entrada2" 
									value="${turnoInstance.dias[row].entrada2}" >
							</td>
							<td>
								<input class="time " 
									type="text" 
									name="dias[${row}].salida2" 
									value="${turnoInstance.dias[row].salida2}" >
							</td>
						</tr>
						</g:each>
					</tbody>
				</table>
			</div>

			
			
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
	</div>
	
</div>

<script type="text/javascript">
	$(document).ready(function(){
  		$('.time').mask('00:00');
	});
</script>