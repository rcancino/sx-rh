<%@page expressionCodec="none"%>
<%@ page import="com.luxsoft.sw4.rh.EjercicioBimestreReportCommand" %>


<div class="modal fade" id="sdiModificacioForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Reporte Modificacion Salarial</h4>
			</div>
			<div class="modal-body">
			  <g:form controller='modificacionSalarial' action="modifcacionSalarialReport" class="form-horizontal">
				<fieldset>
						<legend> Parámetros</legend>
						<f:with bean="${new EjercicioBimestreReportCommand()}">
							<f:field property="ejercicio"   widget-class="form-control"/>
							<f:field property="bimestre"  widget-class="form-control"/>
							<f:field property="inicia">
								<input type="text" class="form-control datepicker" id="fechaField" name="inicia" 
    							value="${g.formatDate(date:new Date(),format:'dd/MM/yyyy') }">
							</f:field>
						</f:with>
				</fieldset>
				<div class="form-group">
				    	<div class="col-sm-offset-2 col-sm-3">
				      		<button type="submit" class="btn btn-default">
				      			<span class="glyphicon glyphicon-cog"></span> Ejecutar
				      		</button>
				    	</div>
				  </div>
				</g:form>
				  
         		
			</div>
			
		</div>
		
	</div>
	
</div>

<script type="text/javascript">
	$(function() {
		$('#fechaField').bootstrapDP({
		    format: 'dd/mm/yyyy',
		    keyboardNavigation: false,
		    forceParse: false,
		    autoclose: true,
		    todayHighlight: true
		});
	});
</script>
