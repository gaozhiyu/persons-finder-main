

## How did you sanitize inputs before sending to the LLM?


In my opinion, it is not valid question to ask from the security perspective, 
because it is really not a concern you hack/broken the LLM, if so, we should be proud of (kidding)

This question is more about whether we can get the correct information from the user for our own buisness

  I did not implement this santiniza method. 
  The way I think, of validating the user input for the hobby, 
    -  Validate the length of the hobby( maybe less than 100 characters)
    -  we can a full list of the common hobbies. (Constant Data)
    -  Send User Input to OpenAI. calling the AI to filter/decide, which type of the hobby from the cnstant list (similar to white listing)


## What are the privacy risks of sending PII (Personally Identifiable Information) like "Name" and "Location" to a third-party model? 
- It might breach the data privacy regulations (like GDPR, CCPA) if the data is sent to a third-party model without proper consent and safeguards.
- The 3rd party model might store the data, which can lead to unauthorized access or data breaches next level 
- The data sent to the third-party model might be used for training their models, which can lead to unintended consequences and potential misuse of the data.

## How would you architect this for a high-security banking app?
- We can store the sensentive inforamtion in our database and generate a internal id for the corelation
- And we replace the sensetive inforation with our internal id in the prompt, and send to the LLM.
- When we get it, we repalce them back


