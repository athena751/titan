package com.hp.app.common.web;

import com.hp.app.dao.PageResults;

public abstract interface IPageResultsRender {

	public abstract <T> String render(PageResults<T> paramPageResults);
}
