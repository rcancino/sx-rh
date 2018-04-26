<%@page expressionCodec="none"%>
<%@ page import="com.luxsoft.sw4.rh.CalendarioDet" %>
<div class="modal fade" id="agregarNominaForm" tabindex="-1"> 
	<div class="modal-dialog modal-lg">
		<div id="modalContent" class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Agregar nomina </h4>
			</div>
			<g:form action="generar" class="form-horizontal" >
				<g:hiddenField name="periodicidad" value="${ periodicidad}" />
				<div class="modal-body">
					<div class="form-group">
    					<label for="tipoField" class="col-sm-3">Tipo</label> 
    					<div class="col-sm-9">
    						<g:select id="tipoField" class="form-control "  
								name="tipo" 
								from="${['GENERAL','ESPECIAL','AGUINALDO','PTU','LIQUIDACION','ESPECIAL_PA']}" 
								/>
    					</div>
  					</div>
  					<div class="form-group">
  						<label for="comentarioField" class="col-sm-3">Calendario  ${ periodicidad} </label>
    					<div class="col-sm-9">
    						%{-- <g:select id="periodoField" class="form-control chosen-select"  
								name="calendarioDet" 
								from="${periodos}"
								value="${periodicidad} - ${calendarioActual} " 
								optionKey="id" 
								
							/> --}%
							<g:hiddenField id="calendarioFieldId" name="calendarioDet"  />
							<input 
								id="calendarioDetField" 
								type="text" 
								name="calendarioField"  
								class="form-control " 
								value="" 
								placeholder="Seleccione un calendario">
							</input>
    					</div>
  					</div>
  					
  					<div class="form-group">
    					<label for="tipoField" class="col-sm-3">Forma de pago</label>
    					<div class="col-sm-9">
    						<g:select id="formaDePagoField" class="form-control"  
								name="formaDePago" 
								from="${['TRANSFERENCIA','CHEQUE']}" 
								/>
    					</div>
  					</div>
  					
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="Agregar"
							value="aceptar" />
				</div>
				
			</g:form>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>

<script type="text/javascript">
	$(function(){
		$("#calendarioDetField").autocomplete({
			source:'<g:createLink controller="calendarioDet" action="getCalendariosDisponibles" params="[periodicidad: periodicidad]" />',
			minLength:1,
			select:function(e,ui){
				$("#calendarioFieldId").val(ui.item.id);
			},
			appendTo: "#modalContent"
		});
	});
</script>
