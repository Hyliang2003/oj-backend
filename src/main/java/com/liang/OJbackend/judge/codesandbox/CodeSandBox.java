package com.liang.OJbackend.judge.codesandbox;

import com.liang.OJbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.liang.OJbackend.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {
    void executeCode(ExecuteCodeRequest executeCodeRequest);
}
