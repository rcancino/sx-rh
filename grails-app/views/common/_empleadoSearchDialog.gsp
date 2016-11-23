<div class="modal fade" id="empleadoSearchDialog" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<g:form controller="empleado" action="empleadoSearch"  class="form-horizontal" >
				<div class="modal-body">
					<f:with bean="${new com.luxsoft.sw4.rh.EmpleadoSerachCommand()}">
						<f:field property="empleado"/>
					</f:with>
					
				</div>
					<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="update" value="Mostrar"/>
				</div>
			</g:form>
		</div><!-- moda-content -->
		
	</div><!-- modal-di -->
	
</div>
