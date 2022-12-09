package ro.project.prototype.application;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerAdviceTest {

  @InjectMocks
  private ExceptionHandlerControllerAdvice underTest;

  @Test
  public void testBuildAndLogExceptionResponse_withMessage() {
    HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
    when(mockedRequest.getRequestURI()).thenReturn("uri");
    when(mockedRequest.getMethod()).thenReturn("GET");

    ExceptionResponse result = underTest.buildAndLogExceptionResponse("Message", new Exception("ex"),
        mockedRequest);

    assertEquals("Message", result.getMessage());
    assertNotNull(result.getTimestamp());
    assertEquals("uri", result.getRequestedUri());
    assertEquals("GET", result.getHttpMethod());
  }

  @Test
  public void testBuildAndLogExceptionResponse_withoutCodeAndMessage() {
    HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
    when(mockedRequest.getRequestURI()).thenReturn("uri");
    when(mockedRequest.getMethod()).thenReturn("GET");

    ExceptionResponse result = underTest.buildAndLogExceptionResponse(null, new Exception("ex"),
        mockedRequest);

    assertEquals("An unexpected internal error has occurred.", result.getMessage());
    assertNotNull(result.getTimestamp());
    assertEquals("uri", result.getRequestedUri());
    assertEquals("GET", result.getHttpMethod());
  }
}
