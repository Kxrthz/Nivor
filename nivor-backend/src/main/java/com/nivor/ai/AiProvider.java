package com.nivor.ai;
public interface AiProvider{boolean available();String providerName();String generate(String prompt,String boundedContext);}
