
function isCaseFromCases(node){
	if(null != node.getParentNode() && node.getParentNode().diyType == 'caseFolder'  && node.diyType == 'case'){
		return true;
	}
	return false;
}

function isCaseFromJob(node){
	if(null != node.getParentNode() && node.getParentNode().diyType == 'testJob' && node.diyType == 'case'){
		return true;
	}
	return false;
}

function isPlanFromJob(node){
	if(null != node.getParentNode() && node.getParentNode().diyType == 'testJob' && node.diyType == 'plan'){
		return true;
	}
	return false;
}

function isCaseFromPlan(node){
	if(node.diyType == 'caseInPlan'){
		return true;
	}
	return false;
}

function isPlanFromPlans(node){
	if(null != node.getParentNode() && node.getParentNode().diyType == 'planFolder' && node.diyType == 'plan'){
		return true;
	}
	return false;
}

function isCaseToCases(node, targetNode){
	if(node.diyType == 'case' && targetNode.diyType == 'caseFolder'){
		return true;
	}
	return false;
}

function isCaseToJob(node, targetNode){
	if(node.diyType == 'case' && targetNode.diyType == 'testJob'){
		return true;
	}
	return false;
}

function isCaseToCase(node, targetNode){
	if(node.diyType == 'case' && targetNode.diyType == 'case'){
		return true;
	}
	return false;
}

function isPlanToJob(node, targetNode){
	if(node.diyType == 'plan' && targetNode.diyType == 'testJob'){
		return true;
	}
	return false;
}

function isPlanToPlans(node, targetNode){
	if(node.diyType == 'plan' && targetNode.diyType == 'planFolder'){
		return true;
	}
	return false;
}

function isPlanToPlan(node, targetNode){
	if(node.diyType == 'plan' && targetNode.diyType == 'plan'){
		return true;
	}
	return false;
}

function isPlanToCase(node, targetNode){
	if(node.diyType == 'plan' && targetNode.diyType == 'case'){
		return true;
	}
	return false;
}

function isCaseToPlan(node, targetNode){
	if(node.diyType == 'case' && targetNode.diyType == 'plan'){
		return true;
	}
	return false;
}

function isCaseToPlans(node, targetNode){
	if(node.diyType == 'case' && targetNode.diyType == 'planFolder'){
		return true;
	}
	return false;
}