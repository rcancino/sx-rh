<lx:ibox>
	<lx:iboxTitle title="Propiedades"/>
	<g:form name="updateForm" class="form-horizontal" action="update" method="PUT">
		
		<lx:iboxContent>
			<g:hiddenField name="id" value="${fonacotInstance.id}" />
			<g:hiddenField name="version" value="${fonacotInstance.version}" />
			
			<f:with bean="${fonacotInstance }" >
				<f:field property="importe"  widget="numeric" widget-class="form-control"/>
				<f:field property="retencionMensual"  widget="numeric" widget-class="form-control"/>
				<f:display property="retencionDiaria" wrapper="bootstrap3"/>
				<f:display property="totalAbonos" wrapper="bootstrap3"/>
				<f:display property="saldo" wrapper="bootstrap3"/>
				<f:field property="activo" wrapper="bootstrap3" widget-class="form-control"/>
			</f:with>
		</lx:iboxContent>

		<lx:iboxFooter>
			<div class="btn-group">
				<lx:backButton label="Prestamos"/>
				<button type="submit" class="btn btn-outline btn-primary">
					<i class="fa fa-floppy-save"></i> Actualizar
				</button>
				<g:if test="${!fonacotInstance.abonos}">
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
 				<li>
 					<g:jasperReport
          			jasper="EstadoDeCuentaFonacot"
          			format="PDF"
          			name="Estado de Cuenta">
          				<g:hiddenField name="ID" value="${fonacotInstance.id}"/>
 					</g:jasperReport>
 				</li>
         	</ul>		
	    </div>
		</lx:iboxFooter>
	
	</g:form>
</lx:ibox>

<g:render template="/common/deleteDialog" bean="${fonacotInstance}"/>

<script type="text/javascript">
	$(function(){
		$('form[name=updateForm]').submit(function(e){
		    console.log("Desablidatndo submit button....");
		    var button=$("#saveBtn");
		    button.attr('disabled','disabled')
		    .html('Procesando...');
		    
		    return true;
		});
		$(".numeric").autoNumeric('init',{vMin:'0'},{vMax:'9999999'});
	});
</script>