<g:set var="procesosControllers" 
    value="${['procesos']}" />
<li class="${procesosControllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-cogs"></i> 
    	<span class="nav-label"> Procesos</span>
    	<span class="fa arrow"></span>
    </a>
    <ul class="nav nav-second-level collapse">
        
        <li class="${webRequest.controllerName=='procesos'?'active':''}">
            <g:link controller="procesos">
                <span class="nav-label">Empleados</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>
        
    </ul>
</li>