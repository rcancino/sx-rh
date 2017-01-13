<g:set var="procesosControllers" 
    value="${['procesos','calculoSdi','modificacionSalarial','aguinaldo','ptu','calculoAnual','exportador']}" />
<li class="${procesosControllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-cogs"></i> 
    	<span class="nav-label"> Procesos</span>
    	<span class="fa arrow"></span>
    </a>
    <ul class="nav nav-second-level collapse">
        
        <li class="${webRequest.controllerName=='procesos' && webRequest.actionName=='empleados' ?'active':''}">
            <g:link controller="procesos" action="empleados">
                <span class="nav-label">Empleados</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
        </li>
        <li class="${webRequest.controllerName=='calculoSdi' ?'active':''}">
            <g:link controller="calculoSdi">
                <span class="nav-label">Calculo Bimestral SDI</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
        </li>
        <li class="${webRequest.controllerName=='modificacionSalarial' ?'active':''}">
            <g:link controller="modificacionSalarial">
                <span class="nav-label">Mod. Salarial</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
        </li>
        <li class="${webRequest.controllerName=='aguinaldo' ?'active':''}">
            <g:link controller="aguinaldo">
                <span class="nav-label">Aguinaldo</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
        </li>
        <li class="${webRequest.controllerName=='ptu' ?'active':''}">
            <g:link controller="ptu">
                <span class="nav-label">PTU</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
        </li>
        <li class="${webRequest.controllerName=='calculoAnual' ?'active':''}">
            <g:link controller="calculoAnual">
                <span class="nav-label">Calculo Anual</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
        </li>
        
        <li class="${webRequest.controllerName=='exportador' ?'active':''}">
            <g:link controller="exportador">
                <span class="nav-label">Layouts</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
        </li>
    </ul>
</li>