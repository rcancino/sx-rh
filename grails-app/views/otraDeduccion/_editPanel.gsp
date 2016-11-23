<lx:ibox>
	<lx:iboxTitle title="Deducción"/>
	<g:form name="updateForm" class="form-horizontal" action="update" method="PUT">
		
		<lx:iboxContent>
			<g:hiddenField name="id" value="${otraDeduccionInstance.id}" />
			<g:hiddenField name="version" value="${otraDeduccionInstance.version}" />
			
			<f:with bean="${otraDeduccionInstance }" >
				<f:field property="tipo" widget-class="form-control chosen-select" />
				<f:field property="importe" widget="money"/>
				<f:display property="totalAbonos" />
				<f:display property="saldo" />
				<f:field property="comentario" widget-class="form-control" />
			</f:with>
		</lx:iboxContent>

		<lx:iboxFooter>
			<div class="btn-group">
				<lx:backButton label="Otras deducciones"/>
				<button type="submit" class="btn btn-outline btn-primary">
					<i class="fa fa-floppy-save"></i> Actualizar
				</button>
				<g:if test="${!otraDeduccionInstance.abonos}">
					<a class="btn btn-danger btn-outline" 
					data-toggle="modal" data-target="#deleteDialog"><i class="fa fa-trash"></i> Eliminar</a> 
				</g:if>
			    
			</div>
			<div class="btn-group">
	        <button type="button" name="reportes"
	                class="btn btn-primary btn-outline dropdown-toggle" data-toggle="dropdown"
	                role="menu">
	                Reportes <span class="caret"></span>
	        </button>
	        <ul class="dropdown-menu">
				<g:jasperReport
          			jasper="EstadoDeCuentaOtraDeduccion"
          			format="PDF"
          			name="Estado de Cuenta">
          			<g:hiddenField name="PRESTAMO_ID" value="${otraDeduccionInstance.id}"/>
 				</g:jasperReport>
         	</ul>		
	    </div>
		</lx:iboxFooter>
	
	</g:form>
</lx:ibox>

<g:render template="/common/deleteDialog" bean="${otraDeduccionInstance}"/>

<script type="text/javascript">
	$(function(){
		$('form[name=updateForm]').submit(function(e){
		    console.log("Desablidatndo submit button....");
		    var button=$("#saveBtn");
		    button.attr('disabled','disabled')
		    .html('Procesando...');
		    
		    return true;
		});
	});
</script>