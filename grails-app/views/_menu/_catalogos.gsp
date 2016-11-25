<g:set var="catalogosControllers" 
    value="${['empleado','departamento','puesto','ubicacion','conceptoDeNomina','calendario','turno','diaFestivo','catalogosDelSat','tablas']}" />
<g:set var="controller" value="${webRequest.controllerName}"/>
<li class="${catalogosControllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-table"></i> 
    	<span class="nav-label"> Catálogos</span>
    	<span class="fa arrow"></span>
    </a>
    <sec:ifAnyGranted roles="RH_USER">
        <ul class="nav nav-second-level collapse">
        	
        	<li class="${webRequest.controllerName=='departamento'?'active':''}">
        	    <g:link controller="departamento" action="index">
        	    	<span class="nav-label">Departamentos</span> 
					<span class="fa fa-angle-right pull-right"></span>
        	    </g:link>
        	    <span class="fa fa-angle-right pull-right"></span>
        	</li>
            
            <li class="${webRequest.controllerName == 'puesto' ? 'active' : ''}">
            	<g:link controller="puesto" action="index">Puestos</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>
            <li class="${webRequest.controllerName == 'ubicacion' ? 'active' : ''}">
            	<g:link controller="ubicacion" action="index">Ubicaciones</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>
            <li class="${controller == 'empleado' ? 'active': ''}">
            	<g:link controller="empleado" action="index">Empleados</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>

            <li class="${webRequest.controllerName == 'conceptoDeNomina' ? 'active' : ''}">
            	<g:link controller="conceptoDeNomina" action="index">Conceptos</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>
            <li class="${webRequest.controllerName == 'calendario' ? 'active' : ''}">
            	<g:link controller="calendario" action="index">Calendarios</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>

            <li class="${webRequest.controllerName == 'turno' ? 'active' : ''}">
            	<g:link controller="turno" action="index">Turno</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>

            <li class="${webRequest.controllerName == 'diaFestivo' ? 'active' : ''}">
            	<g:link controller="diaFestivo" action="index">Dias Festivos</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>

            <li class="divider"></li>

            <li class="${webRequest.controllerName == 'catalogosDelSat' ? 'active' : ''}">
            	<g:link controller="catalogosDelSat" action="index">SAT</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>
            <li class="${webRequest.controllerName == 'tablas' ? 'active' : ''}">
            	<g:link controller="tablas" action="index">Tablas</g:link>
            	<span class="fa fa-angle-right pull-right"></span>
            </li>
            
        </ul>
    </sec:ifAnyGranted>
</li>