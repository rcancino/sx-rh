<%@ page import="com.luxsoft.sw4.rh.tablas.ZonaEconomica" %>



<div class="fieldcontain ${hasErrors(bean: zonaEconomicaInstance, field: 'ejercicio', 'error')} required">
	<label for="ejercicio">
		<g:message code="zonaEconomica.ejercicio.label" default="Ejercicio" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="ejercicio" from="${zonaEconomicaInstance.constraints.ejercicio.inList}" required="" value="${fieldValue(bean: zonaEconomicaInstance, field: 'ejercicio')}" valueMessagePrefix="zonaEconomica.ejercicio"/>

</div>

<div class="fieldcontain ${hasErrors(bean: zonaEconomicaInstance, field: 'clave', 'error')} required">
	<label for="clave">
		<g:message code="zonaEconomica.clave.label" default="Clave" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="clave" required="" value="${zonaEconomicaInstance?.clave}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: zonaEconomicaInstance, field: 'salario', 'error')} required">
	<label for="salario">
		<g:message code="zonaEconomica.salario.label" default="Salario" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="salario" value="${fieldValue(bean: zonaEconomicaInstance, field: 'salario')}" required=""/>

</div>

