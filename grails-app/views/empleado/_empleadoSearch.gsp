<div class="modal fade" id="empleadoSearchDialog" tabindex="-1">
	<div class="modal-dialog ">
		<div id="empleadoModalContent" class="modal-content">

			<g:form controller="empleado" action="show"  class="form-horizontal" >

				<div class="modal-header">
					<h2>Busqueda de empleado</h2>
				</div>
				
				<div class="modal-body">
					<div class="form-group">
						<label class="col-sm-2 control-label" for="searchField">Nombre</label>
						<div class="col-sm-10">
							<g:hiddenField id="empleadoSearchFieldId" name="id"/>
							<input 
								id="empleadoSearchField" 
								type="text" 
								name="empleadoSearchFieldId"  
								class="form-control " 
								placeholder="Digite el nombre"
								autofocus="true">
							</input>
							 
						</div>
					</div>
				</div>
					<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="update" value="Mostrar"/>
				</div>
			</g:form>
		</div>
		
	</div>
	
</div>
<script type="text/javascript">
	$(function(){
		$("#empleadoSearchField").autocomplete({
			source:'<g:createLink controller="empleadoRest" action="getEmpleados"/>',
			minLength:1,
			appendTo: "#empleadoModalContent",
			select:function(e,ui){
				$("#empleadoSearchFieldId").val(ui.item.id);
			},
		});
	});
</script>
