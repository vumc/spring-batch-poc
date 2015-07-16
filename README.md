# Spring-Batch-POC

## Problem Statement

The client has a gap right now in the end to end solution for POS receipt management. The client knows propably about 10% of pos receipts are not being saved properly into CMS store and the loss is occuring somewhere while transferring from DB store to CMS store.  

## Proposed Solution
The DBA team creates a series of scripts to compare the DB store and CMS store to help identify the missing transactions for a given day and populates the delta records into a table. The Dev team needs to come up with a batch process solution that will address the missing transactions and re-submit the transactions to CMS store via Middleware service.


The spring-batch-poc will address the missing records of the hypothetical client use-case using spring-batch.