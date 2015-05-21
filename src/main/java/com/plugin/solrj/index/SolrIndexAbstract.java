//--------------------------------------------------------------------------
// Copyright (c) 2010-2020, En.dennisit or Cn.苏若年
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// Neither the name of the dennisit nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
// Author: dennisit@163.com | dobby | 苏若年
//--------------------------------------------------------------------------
package com.plugin.solrj.index;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public abstract class SolrIndexAbstract implements SolrIndex{

    private final static Logger LOG = Logger.getLogger(SolrIndexAbstract.class);

    protected SolrServer solrServer;


    @Override
    public synchronized void fullIndex() {
        try {
            LOG.info("begin full index create!");
            createFullIndex();
            LOG.info("finish full index create!");
        } catch (Exception e) {
            LOG.error("increment index error, " + e.getMessage(), e);
        }
    }



    @Override
    public synchronized void incrementIndex() {
        try {
            LOG.info("begin increment index create!");
            createIncrementIndex();
            LOG.info("finish increment index create!");
        } catch (Exception e) {
            LOG.error("increment index error, " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized void clearIndex() {
        try {
            LOG.info("begin clear all index !");
            solrServer.deleteByQuery("*:*");
            solrServer.commit(true, true);
            LOG.info("finish clear all index");
        } catch (Exception e) {
            LOG.error("clear all index error, " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized void optimize() {
        try {
            LOG.info("begin optimize index !");
            solrServer.optimize(true, true);
            LOG.info("finish optimize index !");
        } catch (Exception e) {
            LOG.error("optimize index error, " + e.getMessage(), e);
        }
    }


    /**
     * submit document to server
     * @param solrDocumentList
     * @throws Exception
     */
    protected void submitSolrIndex(Collection<SolrInputDocument> solrDocumentList) throws Exception {
        solrServer.add(solrDocumentList);
        solrServer.commit(true, true);
    }

    /**
     * init solr server
     * @param solrUrl
     */
    public void setSolrServer(String solrUrl) {
        this.solrServer = new CommonsHttpSolrServer(getUrl(solrUrl));
    }

    /** get solr address */
    protected URL getUrl(String solrUrl) {
        URL url = null;
        try {
            url = new URL(solrUrl);
        } catch (MalformedURLException e) {
            LOG.error("Error solrUrl");
        }
        return url;
    }

    /**
     * ready for implements in sub class
     * @return
     */
    protected abstract boolean createIncrementIndex();

    /**
     * ready for implements in sub class
     */
    protected abstract void createFullIndex();

}
