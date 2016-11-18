<form  class="form-horizontal " method="post">
	<g:hiddenField name="id" value="${empleadoInstance.id}"/>
	<g:hiddenField name="version" value="${empleadoInstance?.version}" />
	<g:hiddenField name="view" value="salario" />
	<div class="col-md-6">
		<fieldset ${!edit?'disabled=""':''}>
			<f:with bean="empleadoInstance">
				<f:field input-id="periodicidadField" property="salario.periodicidad" widget-class="form-control" wrapper="bootstrap3"/>	
				<g:if test="${empleadoInstance?.salario?.salarioDiario}">
					<f:display property="salario.salarioDiario" widget-class="form-control"  wrapper="bootstrap3"/>	
				</g:if>
				<g:else>
					<f:field property="salario.salarioDiario" input-id="salarioNuevo"
						widget-class="form-control " wrapper="bootstrap3"/>
					<f:field property="salario.salarioVariable" input-id="salarioVariable"
						widget-class="form-control numeric" wrapper="bootstrap3"/>	
				</g:else>
				<f:field property="salario.salarioDiarioIntegrado" label="SDI"
						widget-class="form-control " widget-type="text" widget="money"
						input-readonly="true" wrapper="bootstrap3"/>	
				<f:field property="salario.formaDePago" widget-class="form-control " wrapper="bootstrap3"/>	
				<f:field property="salario.clabe" widget-class="form-control" input-autocomplete="off" label="CLABE" wrapper="bootstrap3"/>
				<f:field property="salario.numeroDeCuenta" widget-class="form-control" input-autocomplete="off" wrapper="bootstrap3"/>	
				<f:field property="salario.banco" widget-class="form-control" wrapper="bootstrap3"/>	
			</f:with>
		</fieldset>
	</div>

	<g:if test="${edit}">
		<div class="form-group">
			<div class="col-sm-offset-8 col-sm-12">
				<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="updateSalario"/>
				<g:link class="btn btn-default" action="salario" id="${empleadoInstance.id}" >Cancelar</g:link>
			</div>
		</div>
	</g:if>

</form>