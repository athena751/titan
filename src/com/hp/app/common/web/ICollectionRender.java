package com.hp.app.common.web;

import java.util.Collection;

public abstract interface ICollectionRender {

	public abstract <T> String render(Collection<T> paramCollection);
}
