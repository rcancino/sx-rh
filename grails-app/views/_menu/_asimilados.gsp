<g:set var="controllers" 
    value="${['nominaAsimilados']}" />
<li class="${controllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-exchange"></i> 
    	<span class="nav-label"> Asimilados</span>
    	<span class="fa arrow"></span>
    </a>
    <ul class="nav nav-second-level collapse">
        
        
        <li class="${webRequest.controllerName=='nominaAsimilados' ?'active':''}">
            <g:link controller="nominaAsimilados">
                <span class="nav-label">Nómina</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
        </li>
        
        
    </ul>
</li>