export class EventInfo {

  constructor(
    public title: string,
    public dateTime: any,
    public startTime: string, 
    public endTime: string,
    public timezone: string, 
    public participants: string[], 
    public description?: string
  ) {}
}