<lx:ibox>
	<lx:iboxTitle title="Propiedades"/>
	<g:form name="updateForm" class="form-horizontal" action="update" method="PUT">
		
		<lx:iboxContent>
			<g:hiddenField name="id" value="${prestamoInstance.id}" />
			<g:hiddenField name="version" value="${prestamoInstance.version}" />
			
			<f:with bean="${prestamoInstance }" >
				<f:display property="empleado" wrapper="bootstrap3"/>
				<f:display property="alta" wrapper="bootstrap3"/>
				<f:display property="fechaDeAutorizacion" wrapper="bootstrap3" label="Autorización" />
				<f:display property="autorizo" wrapper="bootstrap3" />
				<f:display property="importe" widget="money" wrapper="bootstrap3"/>
				<f:display property="tipo" wrapper="bootstrap3"/>
				<f:display property="tasaDescuento" widget="porcentaje" wrapper="bootstrap3"/>
				<f:display property="importeFijo" input-class="form-control" disabled='true'  wrapper="bootstrap3"/>
				<f:field property="comentario" widget-class="form-control" wrapper="bootstrap3"/>
			</f:with>
		</lx:iboxContent>

		<lx:iboxFooter>
			<div class="btn-group">
				<lx:backButton label="Prestamos"/>
				<button type="submit" class="btn btn-outline btn-primary">
					<i class="fa fa-floppy-save"></i> Actualizar
				</button>
				<g:if test="${!prestamoInstance.abonos}">
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
 	          			jasper="EstadoDeCuentaPrestamo"
 	          			format="PDF"
 	          			name="Estado de Cuenta">
 	          			<g:hiddenField name="PRESTAMO_ID" value="${prestamoInstance.id}"/>
 	 				</g:jasperReport>
 				</li>
         	</ul>		
	    </div>
		</lx:iboxFooter>
	
	</g:form>
</lx:ibox>

<g:render template="/common/deleteDialog" bean="${prestamoInstance}"/>

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