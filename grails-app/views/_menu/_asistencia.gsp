<g:set var="asistenciaControllers" 
    value="${['asistencia','incapacidad','incidencia','vacaciones','vacacionesGrupo','asistenciaImss','tiempoExtra']}" />

<g:set var="vacacionesCtrl" value="${ (webRequest.controllerName == 'vacaciones') || (webRequest.controllerName == 'vacacionesGrupo') }"/>
<g:set var="lectora" value="${(webRequest.controllerName == 'asistencia') && (webRequest.actionName == 'lectora')}"/>
<li class="${asistenciaControllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-check"></i> 
    	<span class="nav-label"> Control de asistencia</span>
    	<span class="fa arrow"></span>
    </a>
    <ul class="nav nav-second-level collapse">
        
        <li class="${webRequest.controllerName=='asistencia'?'active':''}">
            <g:link controller="asistencia">
                <span class="nav-label">Asistencias</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${webRequest.controllerName=='incapacidad'?'active':''}">
            <g:link controller="incapacidad">
                <span class="nav-label">Incapacidades</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${webRequest.controllerName=='incidencia'?'active':''}">
            <g:link controller="incidencia">
                <span class="nav-label">Incidencias</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${vacacionesCtrl ? 'active':''}">
            <g:link controller="vacaciones">
                <span class="nav-label">Vacaciones</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>
        <li class="${webRequest.controllerName=='asistenciaImss'?'active':''}">
            <g:link controller="asistenciaImss">
                <span class="nav-label">Asistenaic IMSS</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li >
            <g:link controller="tiempoExtra">
                <span class="nav-label">Tiempo extra</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${lectora ? 'active' : ''}">
            <g:link controller="asistencia" action="lectora">
                <span class="nav-label">Lectora</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>
        
    </ul>
</li>