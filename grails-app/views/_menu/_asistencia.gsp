<g:set var="asistenciaControllers" 
    value="${['vacaciones']}" />
<li class="${asistenciaControllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-check"></i> 
    	<span class="nav-label"> Control de asistencia</span>
    	<span class="fa arrow"></span>
    </a>
    <ul class="nav nav-second-level collapse">
        
        <li class="${webRequest.controllerName=='vacaciones'?'active':''}">
            <g:link controller="vacaciones">
                <span class="nav-label">Vacaciones</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>
        
        
    </ul>
</li>