const sendSuccess = (res, message, data = {}, statusCode = 200) => {
  res.status(statusCode).json({
    success: true,
    message,
    data,
  });
};

const sendError = (res, message, statusCode = 500) => {
  res.status(statusCode).json({
    success: false,
    message,
  });
};

module.exports = {
  sendSuccess,
  sendError,
};
