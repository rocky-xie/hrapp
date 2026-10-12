import axios from 'axios';
import { type IRecruitmentApplication } from '@/shared/model/recruitment-application.model';

const url = 'api/recruitment-workflow';

export default class RecruitmentWorkflowService {
  async listApplications(): Promise<IRecruitmentApplication[]> {
    const response = await axios.get('api/recruitment-applications', { params: { page: 0, size: 100, sort: 'appliedAt,desc' } });
    return response.data;
  }

  async recordStage(id: number, stage: string, selected: boolean, notes: string): Promise<void> {
    await axios.post(`${url}/applications/${id}/stage-result`, { stage, selected, notes });
  }

  async finalDecision(id: number, decision: string, reviewer: string, notes: string): Promise<void> {
    await axios.post(`${url}/applications/${id}/final-decision`, { decision, reviewer, notes });
  }

  async graduateDecision(id: number, decision: string, notes: string): Promise<void> {
    await axios.post(`${url}/applications/${id}/graduate-decision`, { decision, notes });
  }

  async convertGraduate(id: number): Promise<void> {
    await axios.post(`${url}/applications/${id}/convert-graduate`);
  }

  async checkIn(id: number, joinDate: string): Promise<void> {
    await axios.post(`${url}/applications/${id}/check-in`, { joinDate });
  }
}
